package coffeetime.service;

import coffeetime.dto.LoginRequest;
import coffeetime.dto.TokensResponse;
import coffeetime.exception.CoffeeTimeException;
import coffeetime.exception.EntryPayloadCode;
import coffeetime.infrastructure.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

	private final AuthenticationManager authenticationManager;
	private final TokenService tokenService;

	@Transactional
	public TokensResponse loginTokens(LoginRequest request) {
		final Authentication authentication = authenticationManager.authenticate(
			new UsernamePasswordAuthenticationToken(request.username(),
				request.password())
		);
		final CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

		if (!authentication.isAuthenticated()) {
			throw new CoffeeTimeException(EntryPayloadCode.BAD_CREDENTIAL);
		}
		if (userDetails.member() == null) {
			throw new CoffeeTimeException(EntryPayloadCode.NOT_FOUND_USER);
		}

		return tokenService.generateTokens(userDetails.member());
	}
}
