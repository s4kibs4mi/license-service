package ninja.sakib.licenseservice.services;

import lombok.AllArgsConstructor;
import ninja.sakib.licenseservice.daos.UserDao;
import ninja.sakib.licenseservice.exceptions.UserCredentialsIncorrectException;
import ninja.sakib.licenseservice.exceptions.UserNotFoundException;
import ninja.sakib.licenseservice.exceptions.UserNotRegisteredException;
import ninja.sakib.licenseservice.models.User;
import ninja.sakib.licenseservice.models.UserRole;
import ninja.sakib.licenseservice.services.dto.TokenDto;
import ninja.sakib.licenseservice.services.dto.UserDto;
import ninja.sakib.licenseservice.services.dto.UserLoginParams;
import ninja.sakib.licenseservice.services.dto.UserRegisterParams;
import ninja.sakib.licenseservice.shared.security.SecurityService;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserDao userDao;
    private final SecurityService securityService;

    @Override
    public UserDto userRegister(UserRegisterParams params) {
        String hashedPassword = securityService.encryptPassword(params.getPassword());
        User user = User
                .builder()
                .email(params.getEmail())
                .password(hashedPassword)
                .createdAt(Instant.now())
                .role(UserRole.User)
                .build();
        return userDao.save(user).toDto();
    }

    @Override
    public TokenDto userLogin(UserLoginParams params) {
        Optional<User> userO = userDao.findByEmail(params.getEmail());
        if (userO.isEmpty()) {
            throw new UserNotRegisteredException("User with email " + params.getEmail() + " not registered.");
        }

        boolean passwordMatched = securityService.matchPassword(params.getPassword(), userO.get().getPassword());
        if (!passwordMatched) {
            throw new UserCredentialsIncorrectException("Password does not match.");
        }

        var token = securityService.generateToken(userO.get());

        return TokenDto
                .builder()
                .accessToken(token.getAccessToken())
                .refreshToken(token.getRefreshToken())
                .expiresIn(token.getExpiresIn())
                .generatedAt(token.getGeneratedAt())
                .build();
    }

    public UserDto findUserById(String userId) {
        Optional<User> userO = userDao.findById(userId);
        if (userO.isEmpty()) {
            throw new UserNotFoundException("User with id " + userId + " not found.");
        }
        return userO.get().toDto();
    }

    public UserDto updateUserRole(String userId, UserRole newRole) {
        Optional<User> userO = userDao.findById(userId);
        if (userO.isEmpty()) {
            throw new UserNotFoundException("User with id " + userId + " not found.");
        }

        User user = userO.get();
        user = user
                .toBuilder()
                .role(newRole)
                .build();
        return userDao.save(user).toDto();
    }

}
