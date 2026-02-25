package coffeetime.controller.response;

data class TokensResponse(
	val accessToken: String,
	val refreshToken: String
)