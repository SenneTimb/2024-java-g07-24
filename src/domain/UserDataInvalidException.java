package domain;

public class UserDataInvalidException extends Exception {
	public UserDataInvalidException(String message) {
        super(message);
    }

    public UserDataInvalidException(String message, Throwable cause) {
        super(message, cause);
    }
}
