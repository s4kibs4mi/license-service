package ninja.sakib.licenseservice.services;

import lombok.AllArgsConstructor;
import ninja.sakib.licenseservice.daos.PurchaseDao;
import ninja.sakib.licenseservice.daos.UserDao;
import ninja.sakib.licenseservice.exceptions.ContentsNotFoundException;
import ninja.sakib.licenseservice.exceptions.UserNotFoundException;
import ninja.sakib.licenseservice.models.Content;
import ninja.sakib.licenseservice.models.Purchase;
import ninja.sakib.licenseservice.models.User;
import ninja.sakib.licenseservice.services.dto.PurchaseCreateParams;
import ninja.sakib.licenseservice.services.dto.PurchaseDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@AllArgsConstructor
public class PurchaseServiceImpl implements PurchaseService {
    private final PurchaseDao purchaseDao;
    private final ContentService contentService;
    private final UserDao userDao;

    @Override
    public boolean isPurchased(String userId, Content content) {
        return purchaseDao.isPurchased(userId, content.getId());
    }

    @Transactional
    public PurchaseDto purchaseCreate(String userId, PurchaseCreateParams params) {
        List<Content> contentList = contentService.findAll(params.getContentIds());
        List<String> missingItems = params
                .getContentIds()
                .stream()
                .filter(contentId ->
                        contentList
                                .stream()
                                .noneMatch(c -> c.getId().equals(contentId))
                ).toList();

        if (!missingItems.isEmpty()) {
            throw new ContentsNotFoundException(missingItems);
        }

        User currentUser = userDao
                .findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        Purchase purchase = Purchase
                .builder()
                .purchasedBy(currentUser)
                .createdAt(Instant.now())
                .purchasedContents(contentList)
                .build();

        purchase = purchaseDao.save(purchase);
        return purchase.toDto();
    }
}
