package coffeetime.exception;

public class JwtValidationException extends Exception{

	public JwtValidationException(String message, Throwable cause) {
		super(message, cause);
	}
}
