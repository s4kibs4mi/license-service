package ninja.sakib.licenseservice.services.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ContentCreateParams {
    private String content;
}
