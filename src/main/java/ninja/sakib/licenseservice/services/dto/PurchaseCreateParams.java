package ninja.sakib.licenseservice.services.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class PurchaseCreateParams {
    private List<String> contentIds;
}
