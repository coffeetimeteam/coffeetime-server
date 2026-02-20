package coffeetime.controller.response;

public record TokensResponse(
	String accessToken,
	String refreshToken
) {

}
