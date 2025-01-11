package coffeetime.service;

import coffeetime.domain.RefreshToken;
import coffeetime.domain.User;
import coffeetime.dto.AuthResponse;
import coffeetime.dto.RefreshTokenRequest;
import coffeetime.exception.CoffeeTimeException;
import coffeetime.exception.EntryPayloadCode;
import coffeetime.infrastructure.JwtUtility;
import coffeetime.repository.RefreshTokenRepository;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class TokenService {

	@Value("${token.jwt.refresh-token-expiration}")
	private Integer refreshTokenExpiration;
	private final RefreshTokenRepository refreshTokenRepository;
	private final JwtUtility jwtUtility;
	private final PasswordEncoder passwordEncoder;

	public AuthResponse generateTokens(User user) {
		final String accessToken = jwtUtility.generateAccessToken(user);
		final String randomUUID = UUID.randomUUID().toString();
		final long refreshTokenExpirationMills =
			System.currentTimeMillis() + refreshTokenExpiration * 60000L;

		RefreshToken refreshToken = new RefreshToken(
			user,
			passwordEncoder.encode(randomUUID),
			new Date(refreshTokenExpirationMills));
		refreshTokenRepository.save(refreshToken);

		return AuthResponse.of(accessToken, randomUUID);
	}

	public AuthResponse refreshToken(RefreshTokenRequest request) throws CoffeeTimeException {
		String rawRefreshToken = request.refreshToken();
		List<RefreshToken> refreshTokenList = refreshTokenRepository.findByUsername(request.username());
		RefreshToken findRefreshToken = null;
		for (RefreshToken token : refreshTokenList) {
			if (passwordEncoder.matches(rawRefreshToken, token.getToken())) {
				findRefreshToken = token;
			}
		}
		if (findRefreshToken == null) {
			throw new CoffeeTimeException(EntryPayloadCode.NOT_FOUND_TOKEN);
		}
		Date currentDate = new Date();

		if (findRefreshToken.getExpiredAt().before(currentDate)) {
			throw new CoffeeTimeException(EntryPayloadCode.EXPIRED_TOKEN);
		}

		AuthResponse response = generateTokens(findRefreshToken.getUser());
		refreshTokenRepository.delete(findRefreshToken);
		return response;
	}
}
