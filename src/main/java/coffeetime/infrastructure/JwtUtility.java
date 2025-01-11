package coffeetime.infrastructure;

import coffeetime.domain.User;
import coffeetime.exception.CoffeeTimeException;
import coffeetime.exception.EntryPayloadCode;
import coffeetime.exception.JwtValidationException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.crypto.spec.SecretKeySpec;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class JwtUtility {

	private static final String SECRET_KEY_ALGORITHM = "HmacSHA512";

	@Value("${token.jwt.issuer}")
	private String tokenIssuer;

	@Value("${token.jwt.secret}")
	private String secretKey;

	@Value("${token.jwt.access-token-expiration}")
	private Integer accessTokenExpiration;

	public String generateAccessToken(User user) {
		if (user == null || user.getId() == null || user.getUsername() == null) {
			throw new CoffeeTimeException(EntryPayloadCode.NOT_FOUND_USER);
		}

		long expirationTimeInMillis = accessTokenExpiration * 6000 + System.currentTimeMillis();
		String subject = String.format("%s, %s", user.getId(), user.getUsername());
		return Jwts.builder()
			.subject(subject)
			.issuer(tokenIssuer)
			.issuedAt(new Date())
			.expiration(new Date(expirationTimeInMillis))
			.claim("role", user.getRole().name())
			.signWith(Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)), Jwts.SIG.HS512)
			.compact();
	}

	public Claims validateAccessToken(String token) throws JwtValidationException {
		try {
			SecretKeySpec keySpec = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), SECRET_KEY_ALGORITHM);

			return Jwts.parser()
				.verifyWith(keySpec)
				.build()
				.parseSignedClaims(token)
				.getPayload();
		} catch (ExpiredJwtException e) {
			throw new JwtValidationException("Access token expired", e);
		} catch (IllegalArgumentException e) {
			throw new JwtValidationException("Access token is illegal", e);
		} catch (MalformedJwtException e) {
			throw new JwtValidationException("Access token is not well formed", e);
		} catch (UnsupportedJwtException e) {
			throw new JwtValidationException("Access token is not supported", e);
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
