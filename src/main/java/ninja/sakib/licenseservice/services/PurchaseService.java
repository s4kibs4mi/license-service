package ninja.sakib.licenseservice.services;

import ninja.sakib.licenseservice.models.Content;

public interface PurchaseService {
    boolean isPurchased(String userId, Content content);
}
