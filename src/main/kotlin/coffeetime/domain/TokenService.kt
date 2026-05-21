package coffeetime.domain;

import coffeetime.controller.response.TokensResponse;
import coffeetime.repository.RefreshTokenRepository;
import coffeetime.support.auth.JwtUtility;
import coffeetime.support.error.CoffeeTimeException;
import coffeetime.support.error.EntryPayloadCode;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TokenService {

	@Value("${token.jwt.refresh-token-expiration}")
	private Integer refreshTokenExpiration;
	private final RefreshTokenRepository refreshTokenRepository;
	private final JwtUtility jwtUtility;

	@Transactional
	public TokensResponse generateTokens(Member member) {
		final RefreshToken latestToken = refreshTokenRepository.findLatestByMember(member)
			.orElse(null);
		final int newVersion = (latestToken != null) ? latestToken.getTokenVersion() + 1 : 0;
		final String accessToken = jwtUtility.generateAccessToken(member, newVersion);
		final String refreshToken = jwtUtility.generateRefreshToken();
		final long refreshTokenExpirationMills =
			System.currentTimeMillis() + refreshTokenExpiration * 60000L;
		final RefreshToken newRefreshToken = RefreshToken.createRefreshToken(
			member,
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

	private RefreshToken createNewRefreshToken(Member member, String token, int version) {
		final long expirationMills = System.currentTimeMillis() + refreshTokenExpiration * 60000L;
		return RefreshToken.createRefreshToken(
			member,
			token,
			new Date(expirationMills),
			version
		);
	}

	@Transactional
	public TokensResponse renewalTokens(String token) {
		final RefreshToken oldRefreshToken = getValidRefreshToken(token);
		validateExpiration(oldRefreshToken, new Date());

		final RefreshToken updatedRefreshToken = updateTokenVersion(oldRefreshToken);
		final String accessToken = jwtUtility.generateAccessToken(
			updatedRefreshToken.getMember(),
			updatedRefreshToken.getTokenVersion()
		);

		final String newRefreshTokenValue = jwtUtility.generateRefreshToken();
		final RefreshToken newRefreshToken = createNewRefreshToken(
			updatedRefreshToken.getMember(),
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

	@Transactional
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
