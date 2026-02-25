package coffeetime.support.auth

import coffeetime.domain.User
import coffeetime.repository.RefreshTokenRepository
import coffeetime.support.error.CoffeeTimeException
import coffeetime.support.error.EntryPayloadCode
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.nio.charset.StandardCharsets
import java.util.*

@Component
class JwtUtility(
    @param:Value("\${token.jwt.issuer}")
    private val tokenIssuer: String,
    @param:Value("\${token.jwt.secret}")
    private val secretKey: String,
    @param:Value("\${token.jwt.access-token-expiration}")
    private val accessTokenExpiration: Int,
    @param:Value("\${token.jwt.refresh-token-expiration}")
    private val refreshTokenExpiration: Int,
    private val refreshTokenRepository: RefreshTokenRepository
) {

    fun createAccessToken(
        user: User,
        tokenVersion: Int
    ): String {
        val subject = String.format("%s, %s", user.id, user.username)
        return createToken(subject, accessTokenExpiration, user.role.name, tokenVersion)
    }

    fun createRefreshToken(): String {
        return createToken("", refreshTokenExpiration, "", 0)
    }

    private fun createToken(
        subject: String,
        expirationMinutes: Int,
        role: String,
        version: Int
    ): String {
        val expirationTimeInMillis = System.currentTimeMillis() + expirationMinutes * 60 * 1000
        return Jwts.builder()
            .subject(subject)
            .issuer(tokenIssuer)
            .issuedAt(Date())
            .expiration(Date(expirationTimeInMillis))
            .claim("role", role)
            .claim("version", version)
            .signWith(
                Keys.hmacShaKeyFor(secretKey.toByteArray(StandardCharsets.UTF_8)),
                Jwts.SIG.HS512
            )
            .compact()
    }

    fun validateAccessToken(
        token: String
    ): Claims {
        val claims = runCatching {
            val key = Keys.hmacShaKeyFor(secretKey.toByteArray(StandardCharsets.UTF_8))
            Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .payload
        }.getOrElse { throw CoffeeTimeException(EntryPayloadCode.INVALID_TOKEN) }
        val subject = claims.subject ?: throw CoffeeTimeException(EntryPayloadCode.INVALID_TOKEN)
        val parts = subject.split(", ")
        if (parts.size != 2) throw CoffeeTimeException(EntryPayloadCode.INVALID_TOKEN)
        val (userIdStr, _) = parts

        val userId = runCatching { UUID.fromString(userIdStr) }.getOrNull()
            ?: throw CoffeeTimeException(EntryPayloadCode.INVALID_TOKEN)
        val tokenVersion = claims["version"] ?: throw CoffeeTimeException(EntryPayloadCode.INVALID_TOKEN)
        val latestToken = refreshTokenRepository.findLatestRefreshTokenByUserId(userId)
            ?: throw CoffeeTimeException(EntryPayloadCode.INVALID_TOKEN)

        if (latestToken.tokenVersion != tokenVersion) {
            throw CoffeeTimeException(EntryPayloadCode.INVALID_TOKEN)
        }

        return claims
    }
}
