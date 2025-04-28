package ninja.sakib.licenseservice.e2e;

import ninja.sakib.licenseservice.configurations.TestcontainersConfiguration;
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

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class UserControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserDao userDao;

    @BeforeEach
    void setup() {
        userDao.deleteAll();
    }

    @Test
    void userRegisterShouldBeSuccessful() throws Exception {
        JSONObject requestBody = new JSONObject();
        requestBody.put("email", SeedDataHelper.TEST_USER_EMAIL);
        requestBody.put("password", SeedDataHelper.TEST_USER_PASSWORD);

        mockMvc
                .perform(MockMvcRequestBuilders
                        .post(RouteHelper.API_VERSION_V1 + "/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody.toString())
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.email").value(SeedDataHelper.TEST_USER_EMAIL));
    }

    @Test
    void userLoginShouldBeSuccessful() throws Exception {
        SeedDataHelper.registerUser(mockMvc);

        JSONObject requestBody = new JSONObject();
        requestBody.put("email", SeedDataHelper.TEST_USER_EMAIL);
        requestBody.put("password", SeedDataHelper.TEST_USER_PASSWORD);

        mockMvc
                .perform(MockMvcRequestBuilders
                        .post(RouteHelper.API_VERSION_V1 + "/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody.toString())
                )
                .andExpect(status().isOk());
    }
}
