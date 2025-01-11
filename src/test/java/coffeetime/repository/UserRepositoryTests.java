package coffeetime.repository;

import static org.assertj.core.api.Assertions.assertThat;

import coffeetime.domain.User;
import coffeetime.dto.UserCreateRequest;
import coffeetime.service.UserService;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class UserRepositoryTests {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserService userService;

	@Test
	public void testFindUserNotfound() {
		Optional<User> findByUsername = userRepository.findByUsername("notfound@email.com");

		assertThat(findByUsername).isNotPresent();
	}

	@Test
	public void testFindUserFound() {
		// given
		UserCreateRequest request = new UserCreateRequest(
			"found@email.com",
			"password",
			"password"
		);
		userService.createUser(request);

		// when
		Optional<User> foundUser = userRepository.findByUsername("found@email.com");

		// then
		assertThat(foundUser).isPresent();
		assertThat(foundUser.get().getUsername()).isEqualTo("found@email.com");
	}
}
