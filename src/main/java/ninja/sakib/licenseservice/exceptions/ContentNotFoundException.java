package ninja.sakib.licenseservice.exceptions;

public class ContentNotFoundException extends ResourceNotFoundException {
    public ContentNotFoundException(String contentId) {
        super("Content with id " + contentId + " not found");
    }
}
