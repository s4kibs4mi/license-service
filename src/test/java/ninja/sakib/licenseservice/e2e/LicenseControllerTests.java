package ninja.sakib.licenseservice.e2e;

import ninja.sakib.licenseservice.configurations.TestcontainersConfiguration;
import ninja.sakib.licenseservice.daos.ContentDao;
import ninja.sakib.licenseservice.daos.PurchaseDao;
import ninja.sakib.licenseservice.daos.UserDao;
import ninja.sakib.licenseservice.helpers.RouteHelper;
import ninja.sakib.licenseservice.helpers.SeedDataHelper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class LicenseControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserDao userDao;
    @Autowired
    private ContentDao contentDao;
    @Autowired
    private PurchaseDao purchaseDao;

    private String userAccessToken;
    private String adminAccessToken;

    private String testContentId;
    private String testContentIdPurchasedByUser;

    @BeforeEach
    void setup() throws Exception {
        purchaseDao.deleteAll();
        contentDao.deleteAll();
        userDao.deleteAll();

        // Initial data setup
        SeedDataHelper.registerUser(mockMvc, SeedDataHelper.TEST_USER_EMAIL, SeedDataHelper.TEST_USER_PASSWORD);
        userAccessToken = SeedDataHelper.loginUser(mockMvc, SeedDataHelper.TEST_USER_EMAIL, SeedDataHelper.TEST_USER_PASSWORD);

        SeedDataHelper.registerUser(mockMvc, SeedDataHelper.TEST_ADMIN_USER_EMAIL, SeedDataHelper.TEST_ADMIN_USER_PASSWORD);
        adminAccessToken = SeedDataHelper.loginUser(mockMvc, SeedDataHelper.TEST_ADMIN_USER_EMAIL, SeedDataHelper.TEST_ADMIN_USER_PASSWORD);
        SeedDataHelper.userRoleChange(mockMvc, adminAccessToken, "Admin");

        assertNotNull(userAccessToken);
        assertNotNull(adminAccessToken);

        testContentId = SeedDataHelper.contentCreate(mockMvc, adminAccessToken);
        assertNotNull(testContentId);

        testContentIdPurchasedByUser = SeedDataHelper.contentCreate(mockMvc, adminAccessToken);
        assertNotNull(testContentIdPurchasedByUser);
    }

    @Test
    public void licenseShouldBeEligibleForAdminUser() throws Exception {
        mockMvc
                .perform(MockMvcRequestBuilders
                        .get(RouteHelper.API_VERSION_V1 + "/licenses?contentId=" + testContentId)
                        .header("Authorization", "Bearer " + adminAccessToken)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isBoolean())
                .andExpect(jsonPath("$.data").value(true));
    }

    @Test
    public void licenseShouldNotBeEligibleForUser() throws Exception {
        mockMvc
                .perform(MockMvcRequestBuilders
                        .get(RouteHelper.API_VERSION_V1 + "/licenses?contentId=" + testContentId)
                        .header("Authorization", "Bearer " + userAccessToken)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isBoolean())
                .andExpect(jsonPath("$.data").value(false));
    }

    @Test
    public void licenseShouldBeEligibleForUserWhenPurchased() throws Exception {
        JSONArray contentIdsToPurchase = new JSONArray();
        contentIdsToPurchase.put(testContentIdPurchasedByUser);

        JSONObject purchasedContent = new JSONObject();
        purchasedContent.put("content_ids", contentIdsToPurchase);

        mockMvc
                .perform(MockMvcRequestBuilders
                        .post(RouteHelper.API_VERSION_V1 + "/purchases")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(purchasedContent.toString())
                        .header("Authorization", "Bearer " + userAccessToken)
                )
                .andExpect(status().isCreated());

        mockMvc
                .perform(MockMvcRequestBuilders
                        .get(RouteHelper.API_VERSION_V1 + "/licenses?contentId=" + testContentIdPurchasedByUser)
                        .header("Authorization", "Bearer " + userAccessToken)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isBoolean())
                .andExpect(jsonPath("$.data").value(true));
    }
}
