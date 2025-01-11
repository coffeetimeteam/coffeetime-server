package coffeetime.controller;

import coffeetime.dto.APIResponse;
import coffeetime.dto.AuthRequest;
import coffeetime.dto.AuthResponse;
import coffeetime.dto.RefreshTokenRequest;
import coffeetime.exception.CoffeeTimeException;
import coffeetime.exception.EntryPayloadCode;
import coffeetime.infrastructure.CustomUserDetails;
import coffeetime.service.TokenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

	private final AuthenticationManager authenticationManager;
	private final TokenService tokenService;

	@PostMapping("/login")
	public ResponseEntity<APIResponse> getAccessToken(
		@RequestBody @Valid AuthRequest request) {
		try {
			Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(request.username(),
					request.password()));
			CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
			AuthResponse response = tokenService.generateTokens(
				userDetails.user());
			HttpHeaders tokenHeaders = generateToken(response.getAccessToken(),
				response.getRefreshToken());
			return ResponseEntity.ok().headers(tokenHeaders).body(new APIResponse(EntryPayloadCode.SUCCESS_REQUEST));
		} catch (BadCredentialsException e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new APIResponse(EntryPayloadCode.BAD_CREDENTIAL));
		} catch (UsernameNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new APIResponse(EntryPayloadCode.NOT_FOUND_USER));
		}
	}

	@PostMapping("/token")
	public ResponseEntity<?> renewalRefreshToken(
		@RequestBody @Valid RefreshTokenRequest request) {
		AuthResponse response = tokenService.refreshToken(request);
		if (response.getRefreshToken() == null) {
			throw new CoffeeTimeException(EntryPayloadCode.FAIL_RENEWAL_TOKE);
		}
		return ResponseEntity.ok(response);
	}

	private HttpHeaders generateToken(String accessToken, String refreshToken) {
		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
		headers.add(HttpHeaders.AUTHORIZATION, "refreshToken " + refreshToken);
		if (accessToken == null || refreshToken == null) {
			throw new CoffeeTimeException(EntryPayloadCode.NOT_FOUND_TOKEN);
		}
		return headers;
	}
}


