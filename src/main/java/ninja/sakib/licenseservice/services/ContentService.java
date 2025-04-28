package ninja.sakib.licenseservice.services;

import ninja.sakib.licenseservice.models.Content;
import ninja.sakib.licenseservice.services.dto.ContentCreateParams;
import ninja.sakib.licenseservice.services.dto.ContentDto;
import ninja.sakib.licenseservice.services.dto.ContentUpdateParams;

public interface ContentService {
    ContentDto contentCreate(ContentCreateParams params);

    ContentDto contentUpdate(ContentUpdateParams params);

    Content findById(String contentId);
}
