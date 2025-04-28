package ninja.sakib.licenseservice.services.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PurchaseDto {
    private String id;

    public ninja.sakib.licenseservice.controllers.dto.PurchaseDto toDto() {
        return ninja.sakib.licenseservice.controllers.dto.PurchaseDto
                .builder()
                .id(this.id)
                .build();
    }
}
