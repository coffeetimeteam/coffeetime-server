package coffeetime.controller;

import coffeetime.domain.UserTokens;
import coffeetime.dto.GlobalResponse;
import coffeetime.dto.LoginRequest;
import coffeetime.exception.EntryPayloadCode;
import coffeetime.service.AuthService;
import coffeetime.service.TokenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
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
	public ResponseEntity<GlobalResponse> login(@RequestBody @Valid LoginRequest request) {
		final UserTokens userTokens = authService.loginTokens(request);
		final HttpHeaders headers = authService.tokenHeaders(userTokens);
		return ResponseEntity.ok().headers(headers)
			.body(new GlobalResponse(EntryPayloadCode.SUCCESS_REQUEST));
	}

	@PostMapping("/token")
	public ResponseEntity<GlobalResponse> extendLogin(
		@RequestHeader(HttpHeaders.AUTHORIZATION) String bearerToken) {
		final UserTokens userTokens = tokenService.renewalTokens(bearerToken);
		final HttpHeaders headers = authService.tokenHeaders(userTokens);
		return ResponseEntity.ok().headers(headers)
			.body(new GlobalResponse(EntryPayloadCode.SUCCESS_REQUEST));
	}
}

