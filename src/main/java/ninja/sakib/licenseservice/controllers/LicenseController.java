package ninja.sakib.licenseservice.controllers;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import ninja.sakib.licenseservice.helpers.RouteHelper;
import ninja.sakib.licenseservice.services.LicenseService;
import ninja.sakib.licenseservice.shared.dto.ApiSuccessResp;
import ninja.sakib.licenseservice.shared.security.SecurityService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class LicenseController {
    private final SecurityService securityService;
    private final LicenseService licenseService;

    @GetMapping(RouteHelper.API_VERSION_V1 + "licenses")
    public ResponseEntity<ApiSuccessResp<Boolean>> getLicense(@RequestParam("contentId") @NotEmpty String contentId) {
        var loggedUser = securityService.getLoggedUser(SecurityContextHolder.getContext());
        var isEligibleForLicense = licenseService.checkLicense(loggedUser.getUsername(), contentId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiSuccessResp
                        .<Boolean>builder()
                        .data(isEligibleForLicense)
                        .build()
                );
    }

}
