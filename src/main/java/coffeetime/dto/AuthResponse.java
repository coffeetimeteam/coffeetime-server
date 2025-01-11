package coffeetime.dto;

import static lombok.AccessLevel.PRIVATE;

import coffeetime.exception.CoffeeTimeException;
import coffeetime.exception.EntryPayloadCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = PRIVATE)
public class AuthResponse {

	private final String accessToken;
	private final String refreshToken;

	public static AuthResponse of(String accessToken, String refreshToken) {
		if (accessToken == null || refreshToken == null) {
			throw new CoffeeTimeException(EntryPayloadCode.INVALID_TOKEN);
		}
		return new AuthResponse(
			accessToken,
			refreshToken
		);
	}
}
