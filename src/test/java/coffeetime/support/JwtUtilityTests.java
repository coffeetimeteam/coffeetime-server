package coffeetime.support;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import coffeetime.config.SecurityConfig;
import coffeetime.controller.request.LoginRequest;
import coffeetime.controller.request.MemberCreateRequest;
import coffeetime.controller.response.GlobalResponse;
import coffeetime.controller.response.TokensResponse;
import coffeetime.domain.AuthService;
import coffeetime.domain.Member;
import coffeetime.domain.MemberService;
import coffeetime.domain.type.RoleType;
import coffeetime.support.auth.JwtUtility;
import coffeetime.support.error.CoffeeTimeException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest
@Import(SecurityConfig.class)
public class JwtUtilityTests {

	@Autowired
	private JwtUtility jwtUtility;

	@Autowired
	private MemberService memberService;

	@Autowired
	private AuthService authService;

	@BeforeEach
	void setup() {
		jwtUtility.updateTokenIssuer("test_token_issuer");
		jwtUtility.updateAccessTokenExpiration(5000);
		jwtUtility.updateSecretKey(
			"test_access_secret_key_must_be_at_least_32_bytes_long_for_security_12312312312");
	}

	@Test
	public void testGenerateFail() {
		// given
		Member member = null;

		// when & then
		assertThrows(CoffeeTimeException.class,
			() -> jwtUtility.generateAccessToken(member, 0));
	}


	@Test
	public void testGenerateSuccess() {
		// given
		MemberCreateRequest memberCreateRequest = new MemberCreateRequest("test@email.com",
			"12341234",
			"12341234");
		GlobalResponse createMemberResponse = memberService.createMember(memberCreateRequest);
		assertNotNull(createMemberResponse, "createMemberResponse should not be null");

		LoginRequest loginRequest = new LoginRequest("test@email.com", "12341234");
		TokensResponse tokensResponse = authService.loginTokens(loginRequest);
		assertNotNull(tokensResponse, "tokensResponse should not be null");

		Member member = Member.createFromClaims(1L, "test@email.com", RoleType.GENERAL_USER);
		assertNotNull(member, "Member should not be null after login");

		// when
		String token = jwtUtility.generateAccessToken(member, 0);

		// then
		assertNotNull(token);
		System.out.println(token);
	}


	@Test
	public void testValidateFail() {
		// given
		String failAccessToken = "this_is_fail_access_token";

		// when & then
		assertThrows(CoffeeTimeException.class,
			() -> jwtUtility.validateAccessToken(failAccessToken));
	}

	@Test
	public void testValidateSuccess() {
		// given
		MemberCreateRequest memberCreateRequest = new MemberCreateRequest(
			"validate@email.com",
			"12341234",
			"12341234"
		);
		GlobalResponse createMemberResponse = memberService.createMember(memberCreateRequest);
		assertNotNull(createMemberResponse, "createMemberResponse should not be null");
		LoginRequest loginRequest = new LoginRequest("validate@email.com", "12341234");

		// when
		TokensResponse tokensResponse = authService.loginTokens(loginRequest);
		assertNotNull(tokensResponse, "tokensResponse should not be null");

		// then
		assertNotNull(tokensResponse.accessToken());
		assertDoesNotThrow(() -> jwtUtility.validateAccessToken(tokensResponse.accessToken()));
	}
}
