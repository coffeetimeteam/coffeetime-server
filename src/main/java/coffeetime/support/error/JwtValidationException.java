package coffeetime.support.error;

public class JwtValidationException extends Exception {

	public JwtValidationException(String message, Throwable cause) {
		super(message, cause);
	}
}
