package ninja.sakib.licenseservice.services.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TokenDto {
    private String accessToken;
    private String refreshToken;
    private long generatedAt;
    private long expiresIn;
}
