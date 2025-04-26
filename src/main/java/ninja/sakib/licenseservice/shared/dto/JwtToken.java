package ninja.sakib.licenseservice.shared.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class JwtToken {
    private String accessToken;
    private String refreshToken;
    private long expiresIn;
    private long generatedAt;
}
