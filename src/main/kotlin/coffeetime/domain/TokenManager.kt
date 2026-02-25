package coffeetime.domain

import coffeetime.repository.RefreshTokenEntity
import coffeetime.support.auth.JwtUtility
import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class TokenManager(
    @param:Value("\${token.jwt.refresh-token-expiration}")
    private val refreshTokenExpiration: Int,
    private val jwtUtility: JwtUtility
) {

    fun create(user: User): Token {
        return generate(user, 0)
    }

    @Transactional
    fun update(user: User, token: RefreshTokenEntity): Long {
        generate(user, version)
        token.update(token)
        return
    }

    fun refreshTokenExpiration(): Long {
        return System.currentTimeMillis() + refreshTokenExpiration * 60000L
    }

    private fun generate(user: User, version: Int): Token {
        val accessToken = jwtUtility.createAccessToken(user, version)
        val refreshToken = jwtUtility.createRefreshToken()
        return Token(
            accessToken,
            refreshToken
        )
    }
}