package ninja.sakib.licenseservice.shared.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import ninja.sakib.licenseservice.exceptions.UserNotAuthenticatedException;
import ninja.sakib.licenseservice.models.User;
import ninja.sakib.licenseservice.shared.dto.JwtToken;
import ninja.sakib.licenseservice.shared.dto.TokenType;
import ninja.sakib.licenseservice.shared.models.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
public class SecurityServiceImpl implements SecurityService {
    @Value("${security.jwt.secret_key}")
    private String jwtSecretKey;

    @Value("${security.jwt.expiration_time}")
    private String jwtExpirationTime;

    @Override
    public String encryptPassword(String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.encode(password);
    }

    @Override
    public Boolean matchPassword(String password, String hashedPassword) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.matches(password, hashedPassword);
    }

    private Key getSignKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecretKey));
    }

    private JwtParser getParser() {
        return Jwts.parser().setSigningKey(getSignKey()).build();
    }

    private JwtBuilder getBuilder() {
        return Jwts.builder().signWith(getSignKey());
    }

    public JwtToken generateToken(User user) {
        long issuedAt = System.currentTimeMillis();
        long expiresIn = System.currentTimeMillis() + Long.parseLong(jwtExpirationTime);
        String token = getBuilder()
                .subject(user.getId())
                .issuedAt(new Date(issuedAt))
                .expiration(new Date(expiresIn))
                .header()
                .type(TokenType.AuthToken.toString())
                .and()
                .compact();
        String refreshToken = getBuilder()
                .subject(user.getId())
                .issuedAt(new Date(issuedAt))
                .expiration(new Date(expiresIn))
                .header()
                .type(TokenType.RefreshToken.toString())
                .and()
                .compact();

        return JwtToken.builder()
                .accessToken(token)
                .refreshToken(refreshToken)
                .generatedAt(issuedAt)
                .expiresIn(expiresIn)
                .build();
    }

    public Claims parseToken(String token) {
        Jws<Claims> parsedToken = getParser().parseSignedClaims(token);
        return parsedToken.getPayload();
    }

    public boolean isTokenExpired(String token) {
        return parseToken(token).getExpiration().before(new Date());
    }

    public UserDetailsImpl getLoggedUser(SecurityContext securityContext) {
        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) securityContext.getAuthentication();
        if (token == null || !token.isAuthenticated()) {
            throw new UserNotAuthenticatedException("user not authenticated");
        }
        return (UserDetailsImpl) token.getPrincipal();
    }
}
