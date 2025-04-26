package ninja.sakib.licenseservice.services;

import lombok.AllArgsConstructor;
import ninja.sakib.licenseservice.daos.ContentDao;
import ninja.sakib.licenseservice.exceptions.ContentNotFoundException;
import ninja.sakib.licenseservice.models.Content;
import ninja.sakib.licenseservice.services.dto.ContentCreateParams;
import ninja.sakib.licenseservice.services.dto.ContentDto;
import ninja.sakib.licenseservice.services.dto.ContentUpdateParams;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ContentServiceImpl implements ContentService {
    private final ContentDao contentDao;

    @Override
    public ContentDto contentCreate(ContentCreateParams params) {
        Content content = Content
                .builder()
                .content(params.getContent())
                .createdAt(Instant.now())
                .build();
        return contentDao
                .save(content)
                .toDto();
    }

    @Override
    @Transactional
    public ContentDto contentUpdate(ContentUpdateParams params) {
        Optional<Content> content = contentDao.findById(params.getId());
        if (content.isEmpty()) {
            throw new ContentNotFoundException(params.getId());
        }

        Content updateableContent = content
                .get()
                .toBuilder()
                .content(params.getContent())
                .build();

        return contentDao
                .save(updateableContent)
                .toDto();
    }
}
