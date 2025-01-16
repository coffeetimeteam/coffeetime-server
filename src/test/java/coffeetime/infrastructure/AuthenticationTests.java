package coffeetime.infrastructure;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import coffeetime.domain.User;
import coffeetime.dto.GlobalResponse;
import coffeetime.dto.UserCreateRequest;
import coffeetime.exception.EntryPayloadCode;
import coffeetime.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@Import(JwtTokenFilter.class)
public class AuthenticationTests {

	@MockBean
	private AuthenticationManager authenticationManager;

	@MockBean
	private UserService userService;

	@MockBean
	private CustomUserDetails mockUserDetails;

	@BeforeEach
	void setUp() {
		final User mockUser = new User("auth@email.com", "password");
		final CustomUserDetails mockUserDetails = new CustomUserDetails(mockUser);
		mockUserDetails.getUsername();
	}

	@Test
	public void testAuthenticationFail() {
		// given
		when(authenticationManager.authenticate(
			new UsernamePasswordAuthenticationToken("wrong@email.com", "wrongPassword")))
			.thenThrow(new BadCredentialsException("Bad credentials"));

		// then
		assertThrows(BadCredentialsException.class, () -> {
			authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken("wrong@email.com", "wrongPassword"));
		});
	}

	@Test
	public void testAuthenticationSuccess() {
		final String username = "auth@email.com";
		final String password = "password";

		GlobalResponse mockResponse = new GlobalResponse(EntryPayloadCode.SUCCESS_REQUEST);

		Authentication mockAuthentication = new UsernamePasswordAuthenticationToken(
			mockUserDetails,
			password,
			mockUserDetails.getAuthorities()
		);

		when(authenticationManager.authenticate(
			new UsernamePasswordAuthenticationToken(username, password)))
			.thenReturn(mockAuthentication);

		when(userService.createUser(any(UserCreateRequest.class)))
			.thenReturn(mockResponse);

		// when
		Authentication authentication = authenticationManager.authenticate(
			new UsernamePasswordAuthenticationToken(username, password));
		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

		// then
		assertThat(authentication.isAuthenticated()).isTrue();
		assertThat(userDetails.getUsername()).isEqualTo(username);
	}
}