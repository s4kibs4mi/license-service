package ninja.sakib.licenseservice.exceptions;

public class UserCredentialsIncorrectException extends RuntimeException {
    public UserCredentialsIncorrectException(String message) {
        super(message);
    }
}
