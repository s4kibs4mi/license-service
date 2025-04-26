package ninja.sakib.licenseservice.controllers.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.Instant;

@Getter
@Builder
public class ContentDto {
    private String id;
    private String content;
    private Instant createdAt;
}
