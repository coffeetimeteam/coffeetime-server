package coffeetime.infrastructure;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import coffeetime.config.SecurityConfig;
import coffeetime.domain.Member;
import coffeetime.domain.type.LoginType;
import coffeetime.domain.type.RoleType;
import coffeetime.exception.CoffeeTimeException;
import coffeetime.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@Import(SecurityConfig.class)
public class JwtUtilityTests {

	@Autowired
	private JwtUtility jwtUtility;

	@Autowired
	private MemberRepository memberRepository;

	@BeforeEach
	void setup() {
		jwtUtility.updateTokenIssuer("test_token_issuer");
		jwtUtility.updateAccessTokenExpiration(5);
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
		Member createTestMember = Member.createUser(LoginType.EMAIL, "test@email.com",
			"화려한 아메리카노", "12341234", RoleType.GENERAL_USER);
		Member savedMember = memberRepository.save(createTestMember);
		String token = jwtUtility.generateAccessToken(savedMember, 0);

		// when && then
		assertNotNull(token);
		System.out.println(token);
	}


	@Test
	public void testValidateFail() {
		// given
		String failAccessToken = "this_is_fail_access_token";

		// when & then
		assertThrows(CoffeeTimeException.class, () -> {
			jwtUtility.validateAccessToken(failAccessToken);
		});
	}

	@Test
	public void testValidateSuccess() {
		//given
		Member member = Member.createUser(LoginType.EMAIL, "test@email.com",
			"화려한 아메리카노",
			"12341234", RoleType.GENERAL_USER);
		String token = jwtUtility.generateAccessToken(member, 0);

		// then
		assertNotNull(token);
		assertDoesNotThrow(() -> {
			jwtUtility.validateAccessToken(token);
		});
	}
}
