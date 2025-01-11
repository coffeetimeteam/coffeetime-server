package coffeetime.exception;

import static lombok.AccessLevel.PROTECTED;

import java.util.Date;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = PROTECTED)
public class ErrorPayloadProvider {

	private Date timestamp;
	private int status;
	private List<String> errors;
	private String path;

	public ErrorPayloadProvider(final int status, final List<String> errors, final String path) {
		this.timestamp = new Date();
		this.status = status;
		this.errors = errors;
		this.path = path;
	}
}
