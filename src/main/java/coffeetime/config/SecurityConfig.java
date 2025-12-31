package coffeetime.config;

import coffeetime.infrastructure.JwtTokenFilter;
import coffeetime.service.CustomUserDetailsService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@EnableWebSecurity(debug = true)
@RequiredArgsConstructor
public class SecurityConfig {

	private final JwtTokenFilter jwtTokenFilter;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	UserDetailsService userDetailsService() {
		return new CustomUserDetailsService();
	}

	@Bean
	DaoAuthenticationProvider daoAuthenticationProvider() {
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		authenticationProvider.setPasswordEncoder(passwordEncoder());
		authenticationProvider.setUserDetailsService(userDetailsService());
		return authenticationProvider;
	}

	@Bean
	AuthenticationManager authenticationManager(
		AuthenticationConfiguration authenticationConfig) throws Exception {
		return authenticationConfig.getAuthenticationManager();
	}

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http)
		throws Exception {
		http.authorizeHttpRequests(
				auth -> auth
					.requestMatchers("/v3/api-docs/**",
						"/swagger-ui/**",
						"/swagger-ui.html",
						"/webjars/**",
						"/swagger-resources/**")
					.permitAll()
					.requestMatchers("/api/v1/auth/**", "/api/health-check").permitAll()
					.anyRequest().authenticated())
			.csrf(AbstractHttpConfigurer::disable)
			.exceptionHandling(exception -> exception.authenticationEntryPoint(
				(request, response, authException) ->
					response.sendError(
						HttpServletResponse.SC_UNAUTHORIZED,
						authException.getMessage())
			))
			.addFilterBefore(jwtTokenFilter, AuthorizationFilter.class)
			.cors(cors -> cors.configurationSource(
				new CorsConfigurationSource() {
					@Override
					public CorsConfiguration getCorsConfiguration(
						HttpServletRequest request) {
						CorsConfiguration config = new CorsConfiguration();
						config.setAllowedOriginPatterns(
							Arrays.asList(
								"http://localhost:3030",
								"https://coffeetime.parkgadan.com")
						);
						config.setAllowedMethods(
							Collections.singletonList("*"));
						config.setAllowedHeaders(
							Collections.singletonList("*"));
						config.setAllowCredentials(true);
						config.setExposedHeaders(
							List.of("Authorization"));
						config.setMaxAge(86400L);
						return config;
					}
				}));
		return http.build();
	}

}