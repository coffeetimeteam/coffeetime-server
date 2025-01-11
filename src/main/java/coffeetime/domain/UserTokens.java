package coffeetime.domain;

public record UserTokens(
	String accessToken,
	String refreshToken
) {

}
