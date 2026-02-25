package coffeetime.domain

import coffeetime.controller.response.TokensResponse
import coffeetime.repository.RefreshTokenEntity
import coffeetime.repository.RefreshTokenRepository
import coffeetime.support.auth.JwtUtility
import coffeetime.support.error.CoffeeTimeException
import coffeetime.support.error.EntryPayloadCode
import java.util.Date
import lombok.RequiredArgsConstructor
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class TokenService(
	private val refreshTokenRepository: RefreshTokenRepository,
	private val jwtUtility: JwtUtility,
	private val tokenManager: TokenManager
) {

	@Transactional
	fun create(user: User): Token {
		val targetToken = refreshTokenRepository.findLatestRefreshTokenByUserId(user.id)?: null
		if (targetToken != null) {renewal(user, targetToken.token)}

		val token = tokenManager.create(user)
		val expiredAt = tokenManager.refreshTokenExpiration()
		refreshTokenRepository.save(
			RefreshTokenEntity(
				user.id,
				token.refresh,
				Date(expiredAt),
				token.version
			)
		)
		return token
	}

	@Transactional
	fun renewal(user: User, token: String): TokensResponse {
		val tokenString = token.replace("Bearer ", "")
		val targetToken = refreshTokenRepository.findByToken(tokenString)
		if (targetToken.expiredAt.before(Date())) throw CoffeeTimeException(EntryPayloadCode.EXPIRED_TOKEN)
		tokenManager.update(user, token)


		final String accessToken = jwtUtility.generateAccessToken(
			updatedRefreshToken.getMember(),
			updatedRefreshToken.getTokenVersion()
		)

		final String newRefreshTokenValue = jwtUtility.createRefreshToken()
		final RefreshToken newRefreshToken = createNewRefreshToken(
			updatedRefreshToken.getMember(),
			newRefreshTokenValue,
			updatedRefreshToken.getTokenVersion()
		)
		return new TokensResponse(accessToken, newRefreshTokenValue)
	}

	@Transactional
	fun deleteRefreshToken(final String bearerToken) {
		if (bearerToken == null) {
			throw new CoffeeTimeException(EntryPayloadCode.INVALID_TOKEN)
		}
		final String token = bearerToken.replace("Bearer ", "")
		final RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
			.orElseThrow(() -> new CoffeeTimeException(EntryPayloadCode.FAIL_LOGOUT))
		refreshTokenRepository.delete(refreshToken)
	}
}
