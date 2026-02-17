package coffeetime.support.auth;

import coffeetime.domain.Member;
import coffeetime.domain.RefreshToken;
import coffeetime.repository.RefreshTokenRepository;
import coffeetime.support.error.CoffeeTimeException;
import coffeetime.support.error.EntryPayloadCode;
import coffeetime.support.error.JwtValidationException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Optional;
import javax.crypto.spec.SecretKeySpec;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class JwtUtility {

	@Value("${token.jwt.issuer}")
	private String tokenIssuer;

	@Value("${token.jwt.secret}")
	private String secretKey;

	@Value("${token.jwt.access-token-expiration}")
	private Integer accessTokenExpiration;

	@Value("${token.jwt.refresh-token-expiration}")
	private Integer refreshTokenExpiration;

	@Autowired
	private RefreshTokenRepository refreshTokenRepository;

	public JwtUtility() {
	}

	public String generateAccessToken(Member member, Integer tokenVersion) {
		if (member == null || member.getId() == null || member.getUsername() == null) {
			throw new CoffeeTimeException(EntryPayloadCode.NOT_FOUND_USER);
		}
		String subject = String.format("%s, %s", member.getId(), member.getUsername());
		return generateToken(subject, accessTokenExpiration, member.getRole().name(), tokenVersion);
	}

	private String generateToken(String subject, Integer expirationMinutes, String role,
		Integer version) {
		long expirationTimeInMillis = System.currentTimeMillis() + expirationMinutes * 60 * 1000;
		return Jwts.builder()
			.subject(subject)
			.issuer(tokenIssuer)
			.issuedAt(new Date())
			.expiration(new Date(expirationTimeInMillis))
			.claim("role", role)
			.claim("version", version)
			.signWith(Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)),
				Jwts.SIG.HS512)
			.compact();
	}

	public String generateRefreshToken() {
		return generateToken("", refreshTokenExpiration, "", 0);
	}

	public Claims validateAccessToken(String token) throws JwtValidationException {
		try {
			final SecretKeySpec keySpec = new SecretKeySpec(
				secretKey.getBytes(StandardCharsets.UTF_8),
				"HmacSHA512"
			);
			final Claims claims = Jwts.parser()
				.verifyWith(keySpec)
				.build()
				.parseSignedClaims(token)
				.getPayload();
			final String subject = claims.getSubject();
			final String[] parts = subject.split(", ");
			final Integer tokenVersion = claims.get("version", Integer.class);
			if (!subject.contains(", ") || parts.length != 2) {
				throw new CoffeeTimeException(EntryPayloadCode.INVALID_TOKEN);
			}
			if (tokenVersion == null) {
				throw new CoffeeTimeException(EntryPayloadCode.INVALID_TOKEN);
			}
			long userId;
			try {
				userId = Long.parseLong(parts[0]);
			} catch (NumberFormatException e) {
				throw new CoffeeTimeException(EntryPayloadCode.INVALID_TOKEN);
			}
			final Optional<RefreshToken> latestToken = refreshTokenRepository.findLatestByMember(
				Member.builder().id(userId).build()
			);
			if (latestToken.isEmpty() || !latestToken.get().getTokenVersion()
				.equals(tokenVersion)) {
				throw new CoffeeTimeException(EntryPayloadCode.INVALID_TOKEN);
			}
			return claims;
		} catch (CoffeeTimeException e) {
			throw e;
		} catch (Exception e) {
			throw new CoffeeTimeException(EntryPayloadCode.INVALID_TOKEN);
		}
	}

	public void updateTokenIssuer(String tokenIssuer) {
		this.tokenIssuer = tokenIssuer;
	}

	public void updateSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

	public void updateAccessTokenExpiration(Integer accessTokenExpiration) {
		this.accessTokenExpiration = accessTokenExpiration;
	}

}
