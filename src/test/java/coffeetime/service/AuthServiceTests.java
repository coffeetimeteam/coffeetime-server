package coffeetime.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import coffeetime.controller.request.LoginRequest;
import coffeetime.controller.response.TokensResponse;
import coffeetime.domain.AuthService;
import coffeetime.domain.TokenService;
import coffeetime.support.error.CoffeeTimeException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class AuthServiceTests {

	@Autowired
	private AuthService authService;

	@MockBean
	private AuthenticationManager authenticationManager;

	@MockBean
	private TokenService tokenService;

	@Test
	public void testLoginSuccess() {
		// given
		LoginRequest request = new LoginRequest("test@email.com", "password");
		UsernamePasswordAuthenticationToken authentication =
			new UsernamePasswordAuthenticationToken(request.username(), request.password());

		when(authenticationManager.authenticate(any()))
			.thenReturn(authentication);
		when(tokenService.renewalTokens(any()))
			.thenReturn(new TokensResponse("access_token", "refresh_token"));

		// when
		TokensResponse response = authService.loginTokens(request);

		// then
		assertThat(response).isNotNull();
		assertThat(response.accessToken()).isNotNull();
		assertThat(response.refreshToken()).isNotNull();
	}

	@Test
	public void testLoginFailure() {
		// given
		LoginRequest request = new LoginRequest("wrong@email.com", "wrongpass");

		when(authenticationManager.authenticate(any()))
			.thenThrow(new BadCredentialsException("Bad credentials"));

		// then
		assertThrows(CoffeeTimeException.class, () -> {
			authService.loginTokens(request);
		});
	}
} 