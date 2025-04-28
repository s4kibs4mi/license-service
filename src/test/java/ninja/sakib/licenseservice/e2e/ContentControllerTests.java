package ninja.sakib.licenseservice.e2e;

import ninja.sakib.licenseservice.configurations.TestcontainersConfiguration;
import ninja.sakib.licenseservice.daos.ContentDao;
import ninja.sakib.licenseservice.daos.UserDao;
import ninja.sakib.licenseservice.helpers.RouteHelper;
import ninja.sakib.licenseservice.helpers.SeedDataHelper;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class ContentControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserDao userDao;
    @Autowired
    private ContentDao contentDao;

    private String userAccessToken;

    @BeforeEach
    void setup() throws Exception {
        contentDao.deleteAll();
        userDao.deleteAll();

        // Initial data setup
        SeedDataHelper.registerUser(mockMvc, SeedDataHelper.TEST_USER_EMAIL, SeedDataHelper.TEST_USER_PASSWORD);
        userAccessToken = SeedDataHelper.loginUser(mockMvc, SeedDataHelper.TEST_USER_EMAIL, SeedDataHelper.TEST_USER_PASSWORD);

        assertNotNull(userAccessToken);
    }

    @Test
    public void contentCreateShouldBeSuccessful() throws Exception {
        JSONObject requestBody = new JSONObject();
        requestBody.put("content", "Test Content");

        mockMvc
                .perform(MockMvcRequestBuilders
                        .post(RouteHelper.API_VERSION_V1 + "/contents")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody.toString())
                        .header("Authorization", "Bearer " + userAccessToken)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.id").exists());
    }
}
