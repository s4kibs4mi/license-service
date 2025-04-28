package ninja.sakib.licenseservice.exceptions;

import lombok.Getter;

import java.util.List;

@Getter
public class ContentsNotFoundException extends RuntimeException {
    private final List<String> contentIds;

    public ContentsNotFoundException(List<String> contentIds) {
        this.contentIds = contentIds;
    }
}

