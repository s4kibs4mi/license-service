package ninja.sakib.licenseservice.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ninja.sakib.licenseservice.services.dto.PurchaseDto;

import java.time.Instant;
import java.util.List;

@Builder(toBuilder = true)
@Entity
@Getter
@Table(name = "purchases")
@NoArgsConstructor
@AllArgsConstructor
public class Purchase {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "purchased_by", nullable = false)
    private User purchasedBy;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "purchased_contents",
            joinColumns = @JoinColumn(name = "purchase_id"),
            inverseJoinColumns = @JoinColumn(name = "content_id")
    )
    private List<Content> purchasedContents;
    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    public PurchaseDto toDto() {
        return PurchaseDto
                .builder()
                .id(this.id)
                .build();
    }
}
