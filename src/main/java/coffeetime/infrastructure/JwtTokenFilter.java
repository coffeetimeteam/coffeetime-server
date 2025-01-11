package coffeetime.infrastructure;


import coffeetime.controller.ServerAlertController;
import coffeetime.domain.Role;
import coffeetime.domain.User;
import coffeetime.exception.JwtValidationException;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class JwtTokenFilter extends OncePerRequestFilter {

	private static final Logger LOGGER = LoggerFactory.getLogger(
		JwtTokenFilter.class);
	private final JwtUtility jwtUtility;
	private final HandlerExceptionResolver handlerExceptionResolver;
	private ServerAlertController serverAlertController;



	public JwtTokenFilter(JwtUtility jwtUtility,
		@Qualifier("handlerExceptionResolver") HandlerExceptionResolver handlerExceptionResolver) {
		this.jwtUtility = jwtUtility;
		this.handlerExceptionResolver = handlerExceptionResolver;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request,
		HttpServletResponse response, FilterChain filterChain)
		throws ServletException, IOException {

		Authentication authentication = SecurityContextHolder.getContext()
			.getAuthentication();
		LOGGER.debug("Initial authentication state: {}",
			authentication != null ? "Present" : "Null");

		String token = extractToken(request);
		if (token == null) {
			LOGGER.debug("No Authorization Bearer token found");
			filterChain.doFilter(request, response);
			return;
		}

		LOGGER.debug("Processing token: {}",
			token.substring(0, Math.min(10, token.length())) + "...");

		try {
			Claims claims = jwtUtility.validateAccessToken(token);
			LOGGER.debug("JWT Claims: subject={}, role={}",
				claims.get(Claims.SUBJECT), claims.get("role"));

			UserDetails userDetails = createUserDetails(claims);
			LOGGER.debug("Created UserDetails: username={}, authorities={}",
				userDetails.getUsername(),
				userDetails.getAuthorities().stream().map(Object::toString)
					.collect(Collectors.joining(", ")));

			setAuthenticationContext(userDetails, request);
			LOGGER.debug(
				"Authentication context set. Proceeding with filter chain.");
			filterChain.doFilter(request, response);

		} catch (JwtValidationException e) {
			LOGGER.error(e.getMessage(), e);
			handlerExceptionResolver.resolveException(request, response, null,
				e);
		}
	}

	private void setAuthenticationContext(UserDetails userDetails,
		HttpServletRequest request) {
		var authenticationToken = new UsernamePasswordAuthenticationToken(
			userDetails, null, userDetails.getAuthorities());
		authenticationToken.setDetails(
			new WebAuthenticationDetailsSource().buildDetails(request));
		SecurityContextHolder.getContext()
			.setAuthentication(authenticationToken);
		LOGGER.debug("Authentication context set for user: {}",
			userDetails.getUsername());
	}

	private UserDetails createUserDetails(Claims claims) {
		String subject = (String) claims.get(Claims.SUBJECT);
		LOGGER.debug("Processing JWT subject: {}", subject);

		String[] array = subject.split(",");
		Long id = Long.valueOf(array[0]);
		String username = array[1].trim();
		Role role = Role.valueOf((String) claims.get("role"));

		User user = new User(id, username, role);
		return new CustomUserDetails(user);
	}

	private String extractToken(HttpServletRequest request) {
		String header = request.getHeader(HttpHeaders.AUTHORIZATION);

		if (header != null && header.startsWith("Bearer ")) {
			return header.split(" ")[1];
		}

		LOGGER.debug("Bearer token extracted failed");
		return null;
	}
}

