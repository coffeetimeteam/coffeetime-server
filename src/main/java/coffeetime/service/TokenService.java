package coffeetime.service;

import coffeetime.domain.RefreshToken;
import coffeetime.domain.User;
import coffeetime.dto.TokensResponse;
import coffeetime.exception.CoffeeTimeException;
import coffeetime.exception.EntryPayloadCode;
import coffeetime.infrastructure.JwtTokenFilter;
import coffeetime.infrastructure.JwtUtility;
import coffeetime.repository.RefreshTokenRepository;
import coffeetime.repository.UserRepository;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class TokenService {

	private final UserRepository userRepository;
	private final JwtTokenFilter jwtTokenFilter;
	private final UserService userService;
	@Value("${token.jwt.refresh-token-expiration}")
	private Integer refreshTokenExpiration;
	private final RefreshTokenRepository refreshTokenRepository;
	private final JwtUtility jwtUtility;

	public TokensResponse generateTokens(User user) {
		final RefreshToken latestToken = refreshTokenRepository.findLatestByUser(user)
			.orElse(null);
		final int newVersion = (latestToken != null) ? latestToken.getTokenVersion() + 1 : 0;
		final String accessToken = jwtUtility.generateAccessToken(user, newVersion);
		final String refreshToken = jwtUtility.generateRefreshToken();
		final long refreshTokenExpirationMills =
			System.currentTimeMillis() + refreshTokenExpiration * 60000L;
		final RefreshToken newRefreshToken = RefreshToken.createRefreshToken(
			user,
			refreshToken,
			new Date(refreshTokenExpirationMills),
			newVersion);
		refreshTokenRepository.save(newRefreshToken);
		return new TokensResponse(accessToken, refreshToken);
	}

	private RefreshToken getValidRefreshToken(String bearerToken) {
		if (bearerToken == null) {
			throw new CoffeeTimeException(EntryPayloadCode.INVALID_TOKEN);
		}
		final String token = bearerToken.replace("Bearer ", "");
		return refreshTokenRepository.findByToken(token)
			.orElseThrow(() -> new CoffeeTimeException(EntryPayloadCode.FAIL_RENEWAL_TOKE));
	}

	private RefreshToken updateTokenVersion(RefreshToken refreshToken) {
		refreshToken.incrementTokenVersion();
		return refreshTokenRepository.save(refreshToken);
	}

	private RefreshToken createNewRefreshToken(User user, String token, int version) {
		final long expirationMills = System.currentTimeMillis() + refreshTokenExpiration * 60000L;
		return RefreshToken.createRefreshToken(
			user,
			token,
			new Date(expirationMills),
			version
		);
	}

	public TokensResponse renewalTokens(String bearerToken) {
		final RefreshToken oldRefreshToken = getValidRefreshToken(bearerToken);
		validateExpiration(oldRefreshToken, new Date());

		final RefreshToken updatedRefreshToken = updateTokenVersion(oldRefreshToken);
		final String accessToken = jwtUtility.generateAccessToken(
			updatedRefreshToken.getUser(),
			updatedRefreshToken.getTokenVersion()
		);

		final String newRefreshTokenValue = jwtUtility.generateRefreshToken();
		final RefreshToken newRefreshToken = createNewRefreshToken(
			updatedRefreshToken.getUser(),
			newRefreshTokenValue,
			updatedRefreshToken.getTokenVersion()
		);

		refreshTokenRepository.delete(updatedRefreshToken);
		refreshTokenRepository.save(newRefreshToken);

		return new TokensResponse(accessToken, newRefreshTokenValue);
	}

	private void validateExpiration(RefreshToken token, Date currentDate) {
		if (token.getExpiredAt().before(currentDate)) {
			throw new CoffeeTimeException(EntryPayloadCode.EXPIRED_TOKEN);
		}
	}

	public void deleteRefreshToken(final String bearerToken) {
		if (bearerToken == null) {
			throw new CoffeeTimeException(EntryPayloadCode.INVALID_TOKEN);
		}
		final String token = bearerToken.replace("Bearer ", "");
		final RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
			.orElseThrow(() -> new CoffeeTimeException(EntryPayloadCode.FAIL_LOGOUT));
		refreshTokenRepository.delete(refreshToken);
	}
}
