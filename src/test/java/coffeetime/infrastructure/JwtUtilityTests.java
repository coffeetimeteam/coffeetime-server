package coffeetime.infrastructure;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import coffeetime.domain.User;
import coffeetime.domain.type.LoginType;
import coffeetime.domain.type.RoleType;
import coffeetime.exception.JwtValidationException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

public class JwtUtilityTests {

	private static JwtUtility jwtUtility;

	@BeforeAll
	static void setup() {
		jwtUtility = new JwtUtility();
		jwtUtility.updateTokenIssuer("coffeetime");
		jwtUtility.updateAccessTokenExpiration(2);
		jwtUtility.updateSecretKey(
			"coffeetime_access_secret_key_must_be_at_least_32_bytes_long_for_security");
	}

	@Test
	public void testGenerateFail() {
		assertThrows(IllegalArgumentException.class, new Executable() {
			@Override
			public void execute() throws Throwable {
				User user = null;
				jwtUtility.generateAccessToken(user);
			}
		});
	}

	@Test
	public void testGenerateSuccess() {
		User user = new User(1L, LoginType.EMAIL, "test@email.com",
			"화려한 아메리카노", "12341234", RoleType.GENERAL_USER);
		String token = jwtUtility.generateAccessToken(user);
		assertNotNull(token);

		System.out.println(token);
	}

	@Test
	public void testValidateFail() {
		assertThrows(JwtValidationException.class, () -> {
			jwtUtility.validateAccessToken("a.b.c");
		});
	}

	@Test
	public void testValidateSuccess() {
		User user = new User(1L, LoginType.EMAIL, "test@email.com",
			"화려한 아메리카노",
			"12341234", RoleType.GENERAL_USER);
		String token = jwtUtility.generateAccessToken(user);
		assertNotNull(token);
		assertDoesNotThrow(() -> {
			jwtUtility.validateAccessToken(token);
		});
	}
}
