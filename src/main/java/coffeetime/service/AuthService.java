package coffeetime.service;

import coffeetime.dto.AuthRequest;
import coffeetime.dto.AuthResponse;
import coffeetime.exception.CoffeeTimeException;
import coffeetime.exception.EntryPayloadCode;
import coffeetime.infrastructure.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

	private final AuthenticationManager authenticationManager;
	private final TokenService tokenService;

	public AuthResponse getTokens(AuthRequest request) {
		final Authentication authentication = authenticationManager.authenticate(
			new UsernamePasswordAuthenticationToken(request.username(),
				request.password())
		);
		final CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

		if (!authentication.isAuthenticated()) {
			throw new CoffeeTimeException(EntryPayloadCode.BAD_CREDENTIAL);
		}

		if (userDetails.user() == null) {
			throw new CoffeeTimeException(EntryPayloadCode.NOT_FOUND_USER);
		}

		return tokenService.generateTokens(userDetails.user());
	}

}
