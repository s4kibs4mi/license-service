package ninja.sakib.licenseservice.services.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ContentUpdateParams {
    private String id;
    private String content;
}
