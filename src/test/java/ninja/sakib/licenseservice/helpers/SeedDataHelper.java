package ninja.sakib.licenseservice.helpers;

import org.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class SeedDataHelper {
    public static String TEST_USER_EMAIL = "test@test.com";
    public static String TEST_USER_PASSWORD = "12345678";
    public static String TEST_ADMIN_USER_EMAIL = "admin@test.com";
    public static String TEST_ADMIN_USER_PASSWORD = "12345678";

    public static void registerUser(MockMvc mockMvc) throws Exception {
        registerUser(mockMvc, TEST_USER_EMAIL, TEST_USER_PASSWORD);
    }

    public static void registerUser(MockMvc mockMvc, String email, String password) throws Exception {
        JSONObject requestBody = new JSONObject();
        requestBody.put("email", email);
        requestBody.put("password", password);

        mockMvc
                .perform(MockMvcRequestBuilders
                        .post(RouteHelper.API_VERSION_V1 + "/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody.toString())
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.email").value(email));
    }

    public static String loginUser(MockMvc mockMvc) throws Exception {
        return loginUser(mockMvc, SeedDataHelper.TEST_USER_EMAIL, SeedDataHelper.TEST_USER_PASSWORD);
    }

    public static String loginUser(MockMvc mockMvc, String email, String password) throws Exception {
        JSONObject requestBody = new JSONObject();
        requestBody.put("email", email);
        requestBody.put("password", password);

        MvcResult mvcResult = mockMvc
                .perform(MockMvcRequestBuilders
                        .post(RouteHelper.API_VERSION_V1 + "/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody.toString())
                )
                .andExpect(status().isOk())
                .andReturn();

        JSONObject response = new JSONObject(mvcResult.getResponse().getContentAsString());
        return response.getJSONObject("data").getString("access_token");
    }

    public static void userRoleChange(MockMvc mockMvc, String accessToken, String newUserRole) throws Exception {
        JSONObject requestBody = new JSONObject();
        requestBody.put("new_role", newUserRole);

        mockMvc
                .perform(MockMvcRequestBuilders
                        .post(RouteHelper.API_VERSION_V1 + "/users/change-role")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody.toString())
                        .header("Authorization", "Bearer " + accessToken)
                )
                .andExpect(status().isOk());
    }

    public static String contentCreate(MockMvc mockMvc, String accessToken) throws Exception {
        JSONObject requestBody = new JSONObject();
        requestBody.put("content", "New Content");

        MvcResult mvcResult = mockMvc
                .perform(MockMvcRequestBuilders
                        .post(RouteHelper.API_VERSION_V1 + "/contents")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody.toString())
                        .header("Authorization", "Bearer " + accessToken)
                )
                .andExpect(status().isCreated())
                .andReturn();

        JSONObject response = new JSONObject(mvcResult.getResponse().getContentAsString());
        return response.getJSONObject("data").getString("id");
    }
}
