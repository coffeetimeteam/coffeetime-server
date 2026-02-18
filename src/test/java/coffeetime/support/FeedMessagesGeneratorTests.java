package coffeetime.support;

import static org.assertj.core.api.Assertions.assertThat;

import coffeetime.domain.type.CoffeeType;
import coffeetime.support.response.FeedMessagesGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class FeedMessagesGeneratorTests {

	@Autowired
	private FeedMessagesGenerator feedMessages;

	@Test
	public void testGetCurrentMessage() {
		String message = feedMessages.getMessages(4, CoffeeType.COFFEE).get("currentMessage");
		assertThat(message).isNotNull();
		assertThat(message).isIn(
			"오늘 CoffeeTime을 많이 즐겼어요!"
		);
	}

	@Test
	public void testGetEncouragementMessage() {
		String message = feedMessages.getMessages(1, CoffeeType.COFFEE).get("encouragementMessage");
		assertThat(message).isNotNull();
		assertThat(message).isIn(
			"🎵 커피 없인 못 살아 정말 못 살아~ 🎶",
			"에너지 충전 완! 🤩",
			"즐거운 CoffeeTime 🏝"
		);
	}
}
