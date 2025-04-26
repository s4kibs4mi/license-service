package ninja.sakib.licenseservice.services;

import ninja.sakib.licenseservice.models.UserRole;
import ninja.sakib.licenseservice.services.dto.TokenDto;
import ninja.sakib.licenseservice.services.dto.UserDto;
import ninja.sakib.licenseservice.services.dto.UserLoginParams;
import ninja.sakib.licenseservice.services.dto.UserRegisterParams;

public interface UserService {
    UserDto userRegister(UserRegisterParams params);

    TokenDto userLogin(UserLoginParams params);

    UserDto findUserById(String userId);

    UserDto updateUserRole(String userId, UserRole newRole);
}
