package coffeetime.support;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import coffeetime.controller.request.MemberCreateRequest;
import coffeetime.controller.response.GlobalResponse;
import coffeetime.domain.DefaultNickname;
import coffeetime.domain.Member;
import coffeetime.domain.MemberService;
import coffeetime.domain.type.LoginType;
import coffeetime.domain.type.RoleType;
import coffeetime.support.auth.CustomUserDetails;
import coffeetime.support.error.EntryPayloadCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class AuthenticationTests {

	@MockBean
	private AuthenticationManager authenticationManager;

	@MockBean
	private MemberService memberService;

	@MockBean
	private DefaultNickname defaultNickname;

	@MockBean
	private PasswordEncoder passwordEncoder;

	@MockBean
	private CustomUserDetails mockUserDetails;

	@BeforeEach
	void setUp() {
		MemberCreateRequest request = new MemberCreateRequest("auth@email.com", "password",
			"password");
		Member mockMember = Member.create(
			LoginType.EMAIL,
			request.getUsername(),
			defaultNickname.generate(),
			passwordEncoder.encode(request.getPassword()),
			RoleType.GENERAL_USER);
		mockUserDetails = new CustomUserDetails(mockMember);
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
		// given
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

		when(memberService.createMember(any(MemberCreateRequest.class)))
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