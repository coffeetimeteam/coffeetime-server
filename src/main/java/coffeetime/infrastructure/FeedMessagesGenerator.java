package coffeetime.infrastructure;

import coffeetime.domain.FeedMessages;
import coffeetime.domain.type.CoffeeType;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FeedMessagesGenerator implements FeedMessages {

	private final Map<Integer, Map<CoffeeType, List<String>>> currentMessages;
	private final Map<Integer, Map<CoffeeType, List<String>>> encouragementMessages;

	@Override
	public String getCurrentMessage(final Integer count, final CoffeeType coffeeType) {
		final Random random = ThreadLocalRandom.current();
		return currentMessages.get(random.nextInt(currentMessages.size())).get(coffeeType)
			.get(count);
	}

	@Override
	public String getEncouragementMessage(final Integer count, final CoffeeType coffeeType) {
		final Random random = ThreadLocalRandom.current();
		return encouragementMessages.get(random.nextInt(encouragementMessages.size()))
			.get(coffeeType).get(count);
	}
}