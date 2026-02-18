package coffeetime.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import coffeetime.controller.response.TokensResponse;
import coffeetime.domain.Member;
import coffeetime.domain.RefreshToken;
import coffeetime.domain.TokenService;
import coffeetime.domain.type.RoleType;
import coffeetime.repository.RefreshTokenRepository;
import coffeetime.support.auth.JwtUtility;
import coffeetime.support.error.CoffeeTimeException;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;
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

	private Member testMember;
	private String testAccessToken;
	private String testRefreshToken;

	@BeforeEach
	void setUp() {
		testMember = Member.createFromClaims(
			UUID.randomUUID(),
			"test@email.com",
			RoleType.GENERAL_USER
		);

		testAccessToken = jwtUtility.generateAccessToken(testMember, 0);
		testRefreshToken = jwtUtility.generateRefreshToken();
	}

	@Test
	public void testGenerateTokens() {
		// given
		RefreshToken savedToken = RefreshToken.createRefreshToken(testMember, testRefreshToken,
			new Date(System.currentTimeMillis() + 3600000), 0);

		when(refreshTokenRepository.save(any(RefreshToken.class)))
			.thenReturn(savedToken);

		// when
		TokensResponse tokens = tokenService.generateTokens(testMember);

		// then
		assertThat(tokens).isNotNull();
		assertThat(tokens.accessToken()).isNotNull();
		assertThat(tokens.refreshToken()).isNotNull();
		assertThat(tokens.accessToken()).isEqualTo(testAccessToken);
		assertThat(tokens.refreshToken()).isEqualTo(testRefreshToken);
	}

	@Test
	public void testRenewalTokensSuccess() {
		// given
		String bearerToken = "Bearer " + testRefreshToken;
		RefreshToken savedToken = RefreshToken.createRefreshToken(testMember, testRefreshToken,
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
		RefreshToken savedToken = RefreshToken.createRefreshToken(testMember, testRefreshToken,
			new Date(System.currentTimeMillis() + 3600000));

		when(refreshTokenRepository.findByToken(testRefreshToken))
			.thenReturn(Optional.of(savedToken));

		// when
		tokenService.deleteRefreshToken(bearerToken);

		// then
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
