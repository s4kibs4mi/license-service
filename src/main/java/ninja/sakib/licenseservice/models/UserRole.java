package ninja.sakib.licenseservice.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum UserRole {
    Admin("Admin"),
    User("User");

    private final String role;

    UserRole(String role) {
        this.role = role;
    }

    @JsonValue
    public String getValue() {
        return role;
    }

    @JsonCreator
    public static UserRole fromValue(String value) {
        for (UserRole userRole : UserRole.values()) {
            if (userRole.role.equalsIgnoreCase(value)) {
                return userRole;
            }
        }
        throw new IllegalArgumentException("Unknown status: " + value);
    }
}
