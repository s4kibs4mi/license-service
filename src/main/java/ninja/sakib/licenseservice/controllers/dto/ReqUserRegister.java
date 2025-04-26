package ninja.sakib.licenseservice.controllers.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReqUserRegister {
    @NotEmpty
    @Email
    private String email;
    @NotEmpty
    @Size(min = 8, max = 128)
    private String password;
}
