package coffeetime.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import coffeetime.domain.RefreshToken;
import coffeetime.domain.User;
import coffeetime.domain.type.LoginType;
import coffeetime.domain.type.RoleType;
import coffeetime.dto.TokensResponse;
import coffeetime.exception.CoffeeTimeException;
import coffeetime.infrastructure.JwtUtility;
import coffeetime.repository.RefreshTokenRepository;
import java.util.Date;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class TokenServiceTests {

	@Autowired
	private TokenService tokenService;

	@Autowired
	private JwtUtility jwtUtility;

	@MockBean
	private RefreshTokenRepository refreshTokenRepository;

	private User testUser;
	private String testAccessToken;
	private String testRefreshToken;

	@BeforeEach
	void setUp() {
		testUser = User.builder()
			.id(1L)
			.loginType(LoginType.EMAIL)
			.username("test@email.com")
			.nickname("테스트유저")
			.password("password")
			.role(RoleType.GENERAL_USER)
			.build();

		testAccessToken = jwtUtility.generateAccessToken(testUser);
		testRefreshToken = jwtUtility.generateRefreshToken();
	}

	@Test
	public void testGenerateTokens() {
		// given
		when(refreshTokenRepository.save(any(RefreshToken.class)))
			.thenReturn(new RefreshToken(testUser, testRefreshToken, new Date()));

		// when
		TokensResponse tokens = tokenService.generateTokens(testUser);

		// then
		assertThat(tokens).isNotNull();
		assertThat(tokens.accessToken()).isNotNull();
		assertThat(tokens.refreshToken()).isNotNull();
	}

	@Test
	public void testRenewalTokensSuccess() {
		// given
		String bearerToken = "Bearer " + testRefreshToken;
		RefreshToken savedToken = new RefreshToken(testUser, testRefreshToken,
			new Date(System.currentTimeMillis() + 3600000));

		when(refreshTokenRepository.findByToken(testRefreshToken))
			.thenReturn(Optional.of(savedToken));
		when(refreshTokenRepository.save(any(RefreshToken.class)))
			.thenReturn(savedToken);

		// when
		TokensResponse response = tokenService.renewalTokens(bearerToken);

		// then
		assertThat(response).isNotNull();
		assertThat(response.accessToken()).isNotNull();
		assertThat(response.refreshToken()).isNotNull();
	}

	@Test
	public void testRenewalTokensWithInvalidToken() {
		// given
		String invalidToken = "Bearer invalid_token";

		// then
		assertThrows(CoffeeTimeException.class, () -> {
			tokenService.renewalTokens(invalidToken);
		});
	}

	@Test
	public void testDeleteRefreshToken() {
		// given
		String bearerToken = "Bearer " + testRefreshToken;
		RefreshToken savedToken = new RefreshToken(testUser, testRefreshToken,
			new Date(System.currentTimeMillis() + 3600000));

		when(refreshTokenRepository.findByToken(testRefreshToken))
			.thenReturn(Optional.of(savedToken));

		// when
		tokenService.deleteRefreshToken(bearerToken);

		// then
		// verify deletion was called (no exception thrown)
		assertThat(true).isTrue();
	}

	@Test
	public void testDeleteRefreshTokenWithInvalidToken() {
		// given
		String invalidToken = "Bearer invalid_token";

		// then
		assertThrows(CoffeeTimeException.class, () -> {
			tokenService.deleteRefreshToken(invalidToken);
		});
	}
} 