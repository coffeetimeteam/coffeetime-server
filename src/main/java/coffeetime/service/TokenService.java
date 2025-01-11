package coffeetime.service;

import coffeetime.domain.RefreshToken;
import coffeetime.domain.User;
import coffeetime.domain.UserTokens;
import coffeetime.exception.CoffeeTimeException;
import coffeetime.exception.EntryPayloadCode;
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
	@Value("${token.jwt.refresh-token-expiration}")
	private Integer refreshTokenExpiration;
	private final RefreshTokenRepository refreshTokenRepository;
	private final JwtUtility jwtUtility;

	public UserTokens generateTokens(User user) {
		final String accessToken = jwtUtility.generateAccessToken(user);
		final String refreshToken = jwtUtility.generateRefreshToken();
		final UserTokens userTokens = new UserTokens(accessToken, refreshToken);
		final long refreshTokenExpirationMills =
			System.currentTimeMillis() + refreshTokenExpiration * 60000L;
		final RefreshToken newRefreshToken = new RefreshToken(
			user,
			userTokens.refreshToken(),
			new Date(refreshTokenExpirationMills));
		refreshTokenRepository.save(newRefreshToken);
		return userTokens;
	}

	public UserTokens renewalTokens(String bearerToken) {
		if (bearerToken == null) {
			throw new CoffeeTimeException(EntryPayloadCode.INVALID_TOKEN);
		}
		final String token = bearerToken.replace("Bearer ", "");
		final Date currentDate = new Date();
		final RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
			.orElseThrow(() -> new CoffeeTimeException(EntryPayloadCode.FAIL_RENEWAL_TOKE));
		validateExpiration(refreshToken, currentDate);
		refreshTokenRepository.delete(refreshToken);
		return generateTokens(refreshToken.getUser());
	}


	private void validateExpiration(RefreshToken token, Date currentDate) {
		if (token.getExpiredAt().before(currentDate)) {
			throw new CoffeeTimeException(EntryPayloadCode.EXPIRED_TOKEN);
		}
	}
}
