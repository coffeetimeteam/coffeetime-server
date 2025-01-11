package coffeetime.dto;

import static lombok.AccessLevel.PROTECTED;

import coffeetime.exception.EntryPayloadCode;
import java.util.Date;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@NoArgsConstructor(access = PROTECTED)
public class APIResponse {

	private Date timestamp;
	private HttpStatus status;
	private String message;

	public APIResponse(final EntryPayloadCode entryPayloadCode) {
		this.timestamp = new Date();
		this.status = entryPayloadCode.getCode();
		this.message = entryPayloadCode.getMessage();
	}
}
