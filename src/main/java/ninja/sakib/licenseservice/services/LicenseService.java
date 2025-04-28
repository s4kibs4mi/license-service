package ninja.sakib.licenseservice.services;

public interface LicenseService {
    boolean checkLicense(String userId, String contentId);
}
