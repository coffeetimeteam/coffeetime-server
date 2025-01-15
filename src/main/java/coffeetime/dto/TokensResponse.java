package coffeetime.dto;

public record TokensResponse(
	String accessToken,
	String refreshToken
) {

}
