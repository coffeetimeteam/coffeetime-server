package coffeetime.repository;

import static org.assertj.core.api.Assertions.assertThat;

import coffeetime.domain.Member;
import coffeetime.dto.MemberCreateRequest;
import coffeetime.service.MemberService;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class MemberRepositoryTests {

	@Autowired
	private MemberRepository memberRepository;

	@Autowired
	private MemberService memberService;

	@Test
	public void testFindUserNotfound() {
		Optional<Member> findByUsername = memberRepository.findByUsername("notfound@email.com");
		assertThat(findByUsername).isNotPresent();
	}

	@Test
	public void testFindUserFound() {
		// given
		MemberCreateRequest request = new MemberCreateRequest(
			"found@email.com",
			"password",
			"password"
		);
		memberService.createUser(request);

		// when
		Optional<Member> foundUser = memberRepository.findByUsername("found@email.com");

		// then
		assertThat(foundUser).isPresent();
		assertThat(foundUser.get().getUsername()).isEqualTo("found@email.com");
	}
}
