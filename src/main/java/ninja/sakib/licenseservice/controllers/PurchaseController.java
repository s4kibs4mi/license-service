package ninja.sakib.licenseservice.controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import ninja.sakib.licenseservice.controllers.dto.PurchaseDto;
import ninja.sakib.licenseservice.controllers.dto.ReqPurchaseCreate;
import ninja.sakib.licenseservice.helpers.RouteHelper;
import ninja.sakib.licenseservice.services.PurchaseService;
import ninja.sakib.licenseservice.services.dto.PurchaseCreateParams;
import ninja.sakib.licenseservice.shared.dto.ApiSuccessResp;
import ninja.sakib.licenseservice.shared.security.SecurityService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class PurchaseController {
    private final SecurityService securityService;
    private final PurchaseService purchaseService;

    @PostMapping(RouteHelper.API_VERSION_V1 + "purchases")
    public ResponseEntity<ApiSuccessResp<PurchaseDto>> purchaseCreate(@RequestBody @Valid ReqPurchaseCreate req) {
        var loggedUser = securityService.getLoggedUser(SecurityContextHolder.getContext());
        var purchaseRes = purchaseService.purchaseCreate(
                loggedUser.getUsername(),
                PurchaseCreateParams
                        .builder()
                        .contentIds(req.getContentIds().stream().toList())
                        .build()
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiSuccessResp
                        .<PurchaseDto>builder()
                        .data(purchaseRes.toDto())
                        .build()
                );
    }
}
