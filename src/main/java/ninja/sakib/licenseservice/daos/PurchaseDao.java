package ninja.sakib.licenseservice.daos;

import ninja.sakib.licenseservice.models.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PurchaseDao extends JpaRepository<Purchase, String> {
    @Query("""
            SELECT CASE WHEN count (p) > 0 THEN true ELSE false END
            FROM Purchase p
            LEFT JOIN p.purchasedContents c
            WHERE p.purchasedBy.id = :userId
            AND c.id = :contentId
            """)
    boolean isPurchased(@Param("userId") String userId, @Param("contentId") String contentId);
}
