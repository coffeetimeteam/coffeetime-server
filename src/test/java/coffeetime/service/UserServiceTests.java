package coffeetime.service;

import static org.assertj.core.api.Assertions.assertThat;

import coffeetime.dto.GlobalResponse;
import coffeetime.dto.UserCreateRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class UserServiceTests {

	@Autowired
	private UserService userService;

	@Test
	public void testAddUser() {
		// given
		UserCreateRequest request = new UserCreateRequest(
			"add_test_user@email.com",
			"password",
			"password"
		);

		// when
		GlobalResponse globalResponse = userService.createUser(request);

		// then
		assertThat(globalResponse.getStatus()).isEqualTo(200);
	}
}