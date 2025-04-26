package ninja.sakib.licenseservice.controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import ninja.sakib.licenseservice.controllers.dto.ContentDto;
import ninja.sakib.licenseservice.controllers.dto.ReqContentCreate;
import ninja.sakib.licenseservice.controllers.dto.ReqContentUpdate;
import ninja.sakib.licenseservice.helpers.RouteHelper;
import ninja.sakib.licenseservice.services.ContentService;
import ninja.sakib.licenseservice.services.dto.ContentCreateParams;
import ninja.sakib.licenseservice.services.dto.ContentUpdateParams;
import ninja.sakib.licenseservice.shared.dto.ApiSuccessResp;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class ContentController {
    private final ContentService contentService;

    @PostMapping(RouteHelper.API_VERSION_V1 + "contents")
    public ResponseEntity<ApiSuccessResp<ContentDto>> contentCreate(@RequestBody @Valid ReqContentCreate req) {
        var content = contentService.contentCreate(ContentCreateParams
                .builder()
                .content(req.getContent())
                .build()
        );
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiSuccessResp
                        .<ContentDto>builder()
                        .data(content.toDto())
                        .build()
                );
    }

    @PutMapping(RouteHelper.API_VERSION_V1 + "contents/{contentId}")
    public ResponseEntity<ApiSuccessResp<ContentDto>> contentUpdate(@PathVariable String contentId, @RequestBody @Valid ReqContentUpdate req) {
        var content = contentService.contentUpdate(ContentUpdateParams
                .builder()
                .id(contentId)
                .content(req.getContent())
                .build()
        );
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiSuccessResp
                        .<ContentDto>builder()
                        .data(content.toDto())
                        .build()
                );
    }

}
