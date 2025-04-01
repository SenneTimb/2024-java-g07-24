package domain;

public class NoCustomersException extends Exception {
	public NoCustomersException(String message) {
        super(message);
    }

    public NoCustomersException(String message, Throwable cause) {
        super(message, cause);
    }
}
