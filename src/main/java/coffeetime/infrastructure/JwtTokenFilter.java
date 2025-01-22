package coffeetime.infrastructure;

import coffeetime.controller.ServerAlertController;
import coffeetime.domain.User;
import coffeetime.domain.type.RoleType;
import coffeetime.exception.JwtValidationException;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
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

	private static final String BEARER_PREFIX = "Bearer ";
	private static final List<String> TOKEN_ENDPOINTS = Arrays.asList("/logout", "/token");


	public JwtTokenFilter(JwtUtility jwtUtility,
		@Qualifier("handlerExceptionResolver") HandlerExceptionResolver handlerExceptionResolver,
		ServerAlertController serverAlertController) {
		this.jwtUtility = jwtUtility;
		this.handlerExceptionResolver = handlerExceptionResolver;
		this.serverAlertController = serverAlertController;
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
			UserDetails userDetails = createUserDetailsFromClaims(claims);
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
			new User(
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
		var authentication = createAuthentication(userDetails, request);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		log.debug("Authentication context set for user: {}", userDetails.getUsername());
	}

	private Authentication createAuthentication(UserDetails userDetails,
		HttpServletRequest request) {
		var authentication = new UsernamePasswordAuthenticationToken(
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
}