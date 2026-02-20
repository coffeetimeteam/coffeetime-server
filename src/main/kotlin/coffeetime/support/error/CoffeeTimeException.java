package coffeetime.support.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CoffeeTimeException extends RuntimeException {

	private final HttpStatus code;
	private final String message;

	public CoffeeTimeException(final EntryPayloadCode entryPayloadCode) {
		this.code = entryPayloadCode.getCode();
		this.message = entryPayloadCode.getMessage();
	}
}
