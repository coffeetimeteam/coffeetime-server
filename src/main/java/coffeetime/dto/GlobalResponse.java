package coffeetime.dto;

import coffeetime.exception.EntryPayloadCode;
import java.util.Date;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GlobalResponse {

	private Date timestamp;
	private HttpStatus status;
	private String message;

	public GlobalResponse(final EntryPayloadCode entryPayloadCode) {
		this.timestamp = new Date();
		this.status = entryPayloadCode.getCode();
		this.message = entryPayloadCode.getMessage();
	}
}
