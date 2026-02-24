package coffeetime.controller;

import coffeetime.config.SecurityRequiredOperation;
import coffeetime.controller.request.LoginRequest;
import coffeetime.support.response.GlobalResponse;
import coffeetime.controller.response.TokensResponse;
import coffeetime.domain.AuthService;
import coffeetime.domain.TokenService;
import coffeetime.support.error.EntryPayloadCode;
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
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

	private final TokenService tokenService;
	private final AuthService authService;

	@PostMapping("/login")
	public ResponseEntity<TokensResponse> login(@RequestBody @Valid LoginRequest request) {
		final TokensResponse userTokens = authService.loginTokens(request);
		return ResponseEntity.ok().body(userTokens);
	}

	@PostMapping("/logout")
	public ResponseEntity<GlobalResponse> logout(
		@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
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

