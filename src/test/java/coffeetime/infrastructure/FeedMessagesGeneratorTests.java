package coffeetime.infrastructure;

import static org.assertj.core.api.Assertions.assertThat;

import coffeetime.domain.FeedMessages;
import coffeetime.domain.type.CoffeeType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class FeedMessagesGeneratorTests {

	@Autowired
	private FeedMessages feedMessages;

	@Test
	public void testGetCurrentMessage() {
		String message = feedMessages.getCurrentMessage(4, CoffeeType.COFFEE);
		assertThat(message).isNotNull();
		assertThat(message).isIn(
			"Coffee Time을 아직 안 가졌어요",
			"아직 Coffee Time을 갖지 않았어요",
			"Coffee Time을 가지지 않았어요",
			"한 잔도 마시지 않았어요! 🫢",
			"Coffee Time을 깜빡했나요? 🥺",
			"커피 네 잔을 마셨어요!"
		);
	}

	@Test
	public void testGetEncouragementMessage() {
		String message = feedMessages.getEncouragementMessage(1, CoffeeType.COFFEE);
		assertThat(message).isNotNull();
		assertThat(message).isIn(
			"🎵 커피 없인 못 살아 정말 못 살아~ 🎶",
			"에너지 충전! 덜 피곤한 기분 🤩",
			"즐거운 Coffee Time 🏝️"
		);
	}
}