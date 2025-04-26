package ninja.sakib.licenseservice.controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import ninja.sakib.licenseservice.controllers.dto.ReqUserLogin;
import ninja.sakib.licenseservice.controllers.dto.ReqUserRegister;
import ninja.sakib.licenseservice.controllers.dto.ReqUserRoleChange;
import ninja.sakib.licenseservice.controllers.dto.UserDto;
import ninja.sakib.licenseservice.helpers.RouteHelper;
import ninja.sakib.licenseservice.services.UserService;
import ninja.sakib.licenseservice.services.dto.TokenDto;
import ninja.sakib.licenseservice.services.dto.UserLoginParams;
import ninja.sakib.licenseservice.services.dto.UserRegisterParams;
import ninja.sakib.licenseservice.shared.dto.ApiSuccessResp;
import ninja.sakib.licenseservice.shared.security.SecurityService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class UserController {
    private final UserService userService;
    private final SecurityService securityService;

    @PostMapping(RouteHelper.API_VERSION_V1 + "users/register")
    public ResponseEntity<ApiSuccessResp<UserDto>> userRegister(@RequestBody @Valid ReqUserRegister req) {
        var userDto = userService.userRegister(UserRegisterParams
                .builder()
                .email(req.getEmail())
                .password(req.getPassword())
                .build()
        );
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiSuccessResp
                        .<UserDto>builder()
                        .data(userDto.toDto())
                        .build()
                );
    }

    @PostMapping(RouteHelper.API_VERSION_V1 + "users/login")
    public ResponseEntity<ApiSuccessResp<TokenDto>> userLogin(@RequestBody @Valid ReqUserLogin req) {
        var token = userService.userLogin(UserLoginParams
                .builder()
                .email(req.getEmail())
                .password(req.getPassword())
                .build()
        );

        return ResponseEntity
                .ok()
                .body(ApiSuccessResp
                        .<TokenDto>builder()
                        .data(TokenDto
                                .builder()
                                .accessToken(token.getAccessToken())
                                .refreshToken(token.getRefreshToken())
                                .generatedAt(token.getGeneratedAt())
                                .expiresIn(token.getExpiresIn())
                                .build())
                        .build()
                );
    }

    @GetMapping(RouteHelper.API_VERSION_V1 + "users/me")
    public ResponseEntity<ApiSuccessResp<UserDto>> userMe() {
        var loggedUser = securityService.getLoggedUser(SecurityContextHolder.getContext());
        var userDto = userService.findUserById(loggedUser.getUsername());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiSuccessResp
                        .<UserDto>builder()
                        .data(userDto.toDto())
                        .build()
                );
    }

    @PostMapping(RouteHelper.API_VERSION_V1 + "users/change-role")
    public ResponseEntity<ApiSuccessResp<UserDto>> userChangeRole(@RequestBody @Valid ReqUserRoleChange req) {
        var loggedUser = securityService.getLoggedUser(SecurityContextHolder.getContext());
        var userDto = userService.updateUserRole(loggedUser.getUsername(), req.newRole);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiSuccessResp
                        .<UserDto>builder()
                        .data(userDto.toDto())
                        .build()
                );
    }
}
