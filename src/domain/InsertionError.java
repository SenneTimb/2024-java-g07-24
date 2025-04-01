package domain;

public class InsertionError extends Exception {

	public InsertionError() {
		super("Error inserting object to database");
	}
	
	
	public InsertionError(String message) {
		super(message);
	}
}
