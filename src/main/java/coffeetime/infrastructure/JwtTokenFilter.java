package coffeetime.infrastructure;

import coffeetime.controller.ServerAlertController;
import coffeetime.domain.Member;
import coffeetime.domain.RefreshToken;
import coffeetime.domain.type.RoleType;
import coffeetime.exception.CoffeeTimeException;
import coffeetime.exception.EntryPayloadCode;
import coffeetime.exception.JwtValidationException;
import coffeetime.repository.RefreshTokenRepository;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

@Component
@Slf4j
public class JwtTokenFilter extends OncePerRequestFilter {

	private final JwtUtility jwtUtility;
	private final HandlerExceptionResolver handlerExceptionResolver;
	private final ServerAlertController serverAlertController;
	private final RefreshTokenRepository refreshTokenRepository;

	private static final String BEARER_PREFIX = "Bearer ";
	private static final List<String> TOKEN_ENDPOINTS = Arrays.asList("/logout", "/token");

	public JwtTokenFilter(JwtUtility jwtUtility,
		@Qualifier("handlerExceptionResolver") HandlerExceptionResolver handlerExceptionResolver,
		ServerAlertController serverAlertController,
		RefreshTokenRepository refreshTokenRepository) {
		this.jwtUtility = jwtUtility;
		this.handlerExceptionResolver = handlerExceptionResolver;
		this.serverAlertController = serverAlertController;
		this.refreshTokenRepository = refreshTokenRepository;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request,
		HttpServletResponse response, FilterChain filterChain)
		throws ServletException, IOException {
		String token = extractToken(request);
		if (isTokenEndpoint(request)) {
			filterChain.doFilter(request, response);
			return;
		}
		if (token == null) {
			log.debug("No Authorization Bearer token found");
			filterChain.doFilter(request, response);
			return;
		}
		processToken(token, request, response, filterChain);
	}

	private boolean isTokenEndpoint(HttpServletRequest request) {
		String requestUri = request.getRequestURI();
		return TOKEN_ENDPOINTS.stream()
			.anyMatch(requestUri::endsWith);
	}

	private void processToken(String token, HttpServletRequest request,
		HttpServletResponse response, FilterChain filterChain)
		throws ServletException, IOException {
		try {
			Claims claims = jwtUtility.validateAccessToken(token);
			if (isAccessTokenExpired(claims)) {
				throw new CoffeeTimeException(EntryPayloadCode.EXPIRED_TOKEN);
			}

			UserDetails userDetails = createUserDetailsFromClaims(claims);
			CustomUserDetails customUserDetails = (CustomUserDetails) userDetails;

			Integer tokenVersion = claims.get("version", Integer.class);
			RefreshToken latestRefreshToken = refreshTokenRepository.findLatestByMember(
					customUserDetails.member())
				.orElseThrow(() -> new CoffeeTimeException(EntryPayloadCode.INVALID_TOKEN));

			if (tokenVersion == null || !tokenVersion.equals(
				latestRefreshToken.getTokenVersion())) {
				throw new CoffeeTimeException(EntryPayloadCode.INVALID_TOKEN);
			}

			setAuthenticationContext(userDetails, request);
			filterChain.doFilter(request, response);
		} catch (JwtValidationException e) {
			serverAlertController.sendServerAlertMessage(request, e);
			log.error("Token validation failed: {}", e.getMessage());
			handlerExceptionResolver.resolveException(request, response, null, e);
		}
	}

	private UserDetails createUserDetailsFromClaims(Claims claims) {
		String[] subjectParts = extractSubjectParts(claims);
		return new CustomUserDetails(
			Member.createFromClaims(
				Long.valueOf(subjectParts[0]),
				subjectParts[1].trim(),
				RoleType.valueOf((String) claims.get("role"))
			)
		);
	}

	private String[] extractSubjectParts(Claims claims) {
		String subject = claims.getSubject();
		log.debug("Processing JWT subject: {}", subject);
		return subject.split(",");
	}

	private void setAuthenticationContext(UserDetails userDetails, HttpServletRequest request) {
		final Authentication authentication = createAuthentication(userDetails, request);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		log.debug("Authentication context set for user: {}", userDetails.getUsername());
	}

	private Authentication createAuthentication(UserDetails userDetails,
		HttpServletRequest request) {
		final UsernamePasswordAuthenticationToken authentication =
			new UsernamePasswordAuthenticationToken(
				userDetails, null, userDetails.getAuthorities());
		authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
		return authentication;
	}

	private String extractToken(HttpServletRequest request) {
		String header = request.getHeader(HttpHeaders.AUTHORIZATION);
		if (header != null && header.startsWith(BEARER_PREFIX)) {
			return header.substring(BEARER_PREFIX.length());
		}
		log.debug("Bearer token extraction failed");
		return null;
	}

	private boolean isAccessTokenExpired(Claims claims) {
		Date expirationDate = claims.getExpiration();
		return expirationDate.before(new Date());
	}
}