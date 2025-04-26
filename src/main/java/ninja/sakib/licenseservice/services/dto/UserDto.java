package ninja.sakib.licenseservice.services.dto;

import lombok.Builder;
import lombok.Getter;
import ninja.sakib.licenseservice.models.UserRole;

@Getter
@Builder
public class UserDto {
    private String id;
    private String email;
    private UserRole role;

    public ninja.sakib.licenseservice.controllers.dto.UserDto toDto() {
        return ninja.sakib.licenseservice.controllers.dto.UserDto
                .builder()
                .id(this.id)
                .email(this.email)
                .role(this.role)
                .build();
    }
}
