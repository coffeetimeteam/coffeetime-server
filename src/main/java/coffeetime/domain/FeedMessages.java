package coffeetime.domain;

import coffeetime.domain.type.CoffeeType;

public interface FeedMessages {

	String getCurrentMessage(Integer count, CoffeeType type);

	String getEncouragementMessage(Integer count, CoffeeType type);
}
