package ninja.sakib.licenseservice.services;

import ninja.sakib.licenseservice.models.Content;
import ninja.sakib.licenseservice.services.dto.PurchaseCreateParams;
import ninja.sakib.licenseservice.services.dto.PurchaseDto;

public interface PurchaseService {
    boolean isPurchased(String userId, Content content);

    PurchaseDto purchaseCreate(String userId, PurchaseCreateParams params);
}
