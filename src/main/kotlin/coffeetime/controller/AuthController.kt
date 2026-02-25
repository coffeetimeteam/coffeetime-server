package coffeetime.controller

import coffeetime.config.SecurityRequiredOperation;
import coffeetime.controller.request.LoginRequest;
import coffeetime.support.response.GlobalResponse;
import coffeetime.controller.response.TokensResponse;
import coffeetime.domain.AuthService;
import coffeetime.domain.TokenService;
import coffeetime.support.error.EntryPayloadCode;
import coffeetime.support.response.ApiStatus
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
class AuthController(
	private val authService: AuthService,
	private val tokenService: TokenService,
) {

	@PostMapping("/login")
	fun login(@RequestBody @Valid request: LoginRequest): ResponseEntity<TokensResponse>  {
		val userTokens: TokensResponse = authService.loginTokens(request.username, request.password)
		return ResponseEntity.ok().body(userTokens)
	}

	@PostMapping("/logout")
	fun logout(@RequestHeader(HttpHeaders.AUTHORIZATION) token: String): ResponseEntity<ApiStatus> {
		tokenService.deleteRefreshToken(token);
		return ResponseEntity.ok().body(new GlobalResponse(EntryPayloadCode.SUCCESS_LOGOUT));
	}

	@GetMapping("/token")
	public ResponseEntity<TokensResponse> extendLogin(
		@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
		final TokensResponse userTokens = tokenService.renewalTokens(token);
		return ResponseEntity.ok().body(userTokens);
	}

	@GetMapping("/validate")
	@SecurityRequiredOperation
	public ResponseEntity<GlobalResponse> validateToken() {
		return ResponseEntity.ok().body(new GlobalResponse(EntryPayloadCode.SUCCESS_REQUEST));
	}
}

