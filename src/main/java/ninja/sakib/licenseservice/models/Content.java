package ninja.sakib.licenseservice.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ninja.sakib.licenseservice.services.dto.ContentDto;
import org.springframework.data.annotation.CreatedDate;

import java.time.Instant;

@Builder(toBuilder = true)
@Entity
@Getter
@Table(name = "contents")
@NoArgsConstructor
@AllArgsConstructor
public class Content {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false, updatable = false)
    private String id;
    private String content;
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    public ContentDto toDto() {
        return ContentDto
                .builder()
                .id(this.id)
                .content(this.content)
                .createdAt(this.createdAt)
                .build();
    }
}
