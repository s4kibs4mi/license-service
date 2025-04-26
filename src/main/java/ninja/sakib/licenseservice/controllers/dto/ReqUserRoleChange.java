package ninja.sakib.licenseservice.controllers.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.Setter;
import ninja.sakib.licenseservice.models.UserRole;

@Getter
@Setter
public class ReqUserRoleChange {
    @JsonProperty("new_role")
    @Valid
    public UserRole newRole;
}
