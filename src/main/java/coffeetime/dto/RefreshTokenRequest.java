package coffeetime.dto;

import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record RefreshTokenRequest(
	@NotNull
	String username,
	@NotNull
	@Length(min = 36, max = 50)
	String refreshToken
) {

}
