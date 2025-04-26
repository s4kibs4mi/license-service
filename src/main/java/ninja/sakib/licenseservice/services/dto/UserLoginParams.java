package ninja.sakib.licenseservice.services.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserLoginParams {
    private final String email;
    private final String password;
}
