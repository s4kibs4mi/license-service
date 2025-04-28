package ninja.sakib.licenseservice.services;

import lombok.AllArgsConstructor;
import ninja.sakib.licenseservice.daos.PurchaseDao;
import ninja.sakib.licenseservice.models.Content;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PurchaseServiceImpl implements PurchaseService {
    private final PurchaseDao purchaseDao;

    @Override
    public boolean isPurchased(String userId, Content content) {
        return purchaseDao.isPurchased(userId, content.getId());
    }
}
