package ninja.sakib.licenseservice.shared.security;

import io.jsonwebtoken.Claims;
import ninja.sakib.licenseservice.models.User;
import ninja.sakib.licenseservice.shared.dto.JwtToken;
import ninja.sakib.licenseservice.shared.models.UserDetailsImpl;
import org.springframework.security.core.context.SecurityContext;

public interface SecurityService {
    String encryptPassword(String password);

    Boolean matchPassword(String password, String hashedPassword);

    JwtToken generateToken(User user);

    Claims parseToken(String token);

    boolean isTokenExpired(String token);

    UserDetailsImpl getLoggedUser(SecurityContext securityContext);
}
