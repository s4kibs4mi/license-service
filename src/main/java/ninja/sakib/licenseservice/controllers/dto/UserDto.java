package ninja.sakib.licenseservice.controllers.dto;

import lombok.Builder;
import lombok.Getter;
import ninja.sakib.licenseservice.models.UserRole;

@Getter
@Builder
public class UserDto {
    private String id;
    private String email;
    private UserRole role;
}
