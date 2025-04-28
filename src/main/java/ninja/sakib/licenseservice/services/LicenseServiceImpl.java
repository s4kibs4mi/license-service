package ninja.sakib.licenseservice.services;

import lombok.AllArgsConstructor;
import ninja.sakib.licenseservice.models.Content;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class LicenseServiceImpl implements LicenseService {
    private final UserService userService;
    private final PurchaseService purchaseService;
    private final ContentService contentService;

    @Override
    public boolean checkLicense(String userId, String contentId) {
        Content content = contentService.findById(contentId);

        if (userService.isLoggedUserAdmin()) {
            return true;
        }

        return purchaseService.isPurchased(userId, content);
    }
}
