package coffeetime.repository;

import static org.assertj.core.api.Assertions.assertThat;

import coffeetime.domain.RefreshToken;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@ExtendWith(SpringExtension.class)
public class RefreshTokenRepositoryTests {

	@Autowired
	private RefreshTokenRepository refreshTokenRepository;

	TestEntityManager testEntityManager;

	@Test
	public void testFindByUsernameNotFound() {
		// given
		String usernameNotExist = "not@email.com";

		// when
		List<RefreshToken> isFindResult = refreshTokenRepository.findByUsername(usernameNotExist);

		// then
		assertThat(isFindResult).isEmpty();
	}

	@Test
	public void testDeleteByExpiryTime() {
		int rowsDeleted = refreshTokenRepository.deleteByExpiredAt();

		assertThat(rowsDeleted).isEqualTo(1);
	}
}

