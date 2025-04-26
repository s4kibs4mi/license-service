package ninja.sakib.licenseservice.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ninja.sakib.licenseservice.services.dto.UserDto;
import org.springframework.data.annotation.CreatedDate;

import java.time.Instant;

@Builder
@Entity
@Getter
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false, updatable = false)
    private String id;
    @Column(unique = true, nullable = false)
    private String email;
    private String password;
    @Enumerated(EnumType.STRING)
    private UserRole role;
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    public UserDto toDto() {
        return UserDto
                .builder()
                .id(this.id)
                .email(this.email)
                .role(this.role)
                .build();
    }

    public UserBuilder toBuilder() {
        UserBuilder builder = new UserBuilder();
        builder.id(this.id);
        builder.email(this.email);
        builder.role(this.role);
        builder.createdAt(this.createdAt);
        return builder;
    }
}
