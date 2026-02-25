package coffeetime.config

import coffeetime.domain.CustomUserDetailsService
import coffeetime.support.auth.JwtTokenFilter
import jakarta.servlet.http.HttpServletResponse
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.access.intercept.AuthorizationFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource

@Configuration
@EnableWebSecurity(debug = true)
class SecurityConfig(
	private val jwtTokenFilter: JwtTokenFilter,
	private val customUserDetailsService: CustomUserDetailsService
) {

	@Bean
	fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()

	@Bean
	fun daoAuthenticationProvider(
		passwordEncoder: PasswordEncoder,
	): DaoAuthenticationProvider = DaoAuthenticationProvider().apply {
			this.setPasswordEncoder(passwordEncoder)
			this.setUserDetailsService(customUserDetailsService)
	}

	@Bean
	fun authenticationManager(
		authenticationConfig: AuthenticationConfiguration
	): AuthenticationManager = authenticationConfig.authenticationManager

	@Bean
	 fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
		http
			.authorizeHttpRequests {
				it.requestMatchers(
				"/v3/api-docs/**",
				"/swagger-ui/**",
				"/swagger-ui.html",
				"/webjars/**",
				"/swagger-resources/**"
				).permitAll()
				it.requestMatchers(
					"/api/v1/auth/**",
					"/api/health-check"
				).permitAll()
				it.anyRequest().authenticated()
			}
			.csrf { it.disable() }
			.exceptionHandling {
				it.authenticationEntryPoint {req, res, authException ->
						res.sendError(
							HttpServletResponse.SC_UNAUTHORIZED,
							authException!!.message
						)
					}
			}
			.addFilterBefore(jwtTokenFilter, AuthorizationFilter::class.java)
			.cors { it.configurationSource(corsConfigurationSource()) }
		return http.build()
	 }

	@Bean
	fun corsConfigurationSource(): CorsConfigurationSource = CorsConfigurationSource {
		CorsConfiguration().apply {
			allowedOriginPatterns = listOf(
				"http://localhost:3030",
				"https://coffeetime.parkgadan.com"
			)
			allowedMethods = listOf("*")
			allowedHeaders = listOf("*")
			allowCredentials = true
			exposedHeaders = listOf("Authorization")
			maxAge = 86400
		}
	}
}