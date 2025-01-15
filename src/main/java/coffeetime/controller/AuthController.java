package coffeetime.controller;

import coffeetime.dto.LoginRequest;
import coffeetime.dto.TokensResponse;
import coffeetime.service.AuthService;
import coffeetime.service.TokenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
		return ResponseEntity.ok(userTokens);
	}

	@GetMapping("/token")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<TokensResponse> extendLogin(
		@RequestHeader(HttpHeaders.AUTHORIZATION) String bearerToken) {
		final TokensResponse userTokens = tokenService.renewalTokens(bearerToken);
		return ResponseEntity.ok(userTokens);
	}
}

