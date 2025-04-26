package ninja.sakib.licenseservice.controllers.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReqUserLogin {
    @NotEmpty
    @Email
    private String email;
    @NotEmpty
    private String password;
}
