package coffeetime.infrastructure;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import coffeetime.dto.UserCreateRequest;
import coffeetime.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class AuthenticationTests {

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	private UserService userService;

	@Test
	public void testAuthenticationFail() {
		assertThrows(BadCredentialsException.class, () -> {
			authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken("wrong@email.com",
					"wrongPassword"));
		});
	}

	@Test
	public void testAuthenticationSuccess() {
		// given
		final String username = "auth@email.com";
		final String password = "password";

		UserCreateRequest request = new UserCreateRequest(
			username,
			password,
			password
		);
		userService.createUser(request);

		// when
		Authentication authentication = authenticationManager.authenticate(
			new UsernamePasswordAuthenticationToken(username, password));
		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

		// then
		assertThat(authentication.isAuthenticated()).isTrue();
		assertThat(userDetails.getUsername()).isEqualTo(username);
	}
}
