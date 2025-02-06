package coffeetime.service;

import static org.assertj.core.api.Assertions.assertThat;

import coffeetime.dto.GlobalResponse;
import coffeetime.dto.MemberCreateRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class MemberServiceTests {

	@Autowired
	private MemberService memberService;

	@Test
	public void testAddUser() {
		// given
		MemberCreateRequest request = new MemberCreateRequest(
			"add_test_user@email.com",
			"password",
			"password"
		);

		// when
		GlobalResponse globalResponse = memberService.createUser(request);

		// then
		assertThat(globalResponse.getStatus()).isEqualTo(200);
	}
}