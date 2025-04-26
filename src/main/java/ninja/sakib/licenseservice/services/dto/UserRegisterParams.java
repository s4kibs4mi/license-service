package ninja.sakib.licenseservice.services.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserRegisterParams {
    private String email;
    private String password;
}
