package ninja.sakib.licenseservice.services.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.Instant;

@Getter
@Builder
public class ContentDto {
    private String id;
    private String content;
    private Instant createdAt;

    public ninja.sakib.licenseservice.controllers.dto.ContentDto toDto() {
        return ninja.sakib.licenseservice.controllers.dto.ContentDto
                .builder()
                .id(this.id)
                .content(this.content)
                .createdAt(this.createdAt)
                .build();
    }
}
