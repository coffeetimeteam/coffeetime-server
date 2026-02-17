package coffeetime.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import coffeetime.domain.DefaultNickname;
import coffeetime.support.response.DefaultNicknameGenerator;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;


@SpringBootTest
@ActiveProfiles("test")
class DefaultNicknameConfigTests {

	@Test
	void createDefaultNickname() {
		//given
		DefaultNickname defaultNickname = new DefaultNicknameGenerator(
			List.of("화려한"), List.of("아메리카노"));

		// when
		String nickname = defaultNickname.generate();

		// then
		assertFalse(nickname.isBlank(), "생성된 nickname은 글자를 포함한다.");
		assertEquals(nickname, "화려한 아메리카노");

		System.out.println("Generated Nickname: " + nickname);
	}
}