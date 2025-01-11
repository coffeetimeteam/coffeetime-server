package coffeetime.infrastructure;

import coffeetime.domain.DefaultNickname;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DefaultNicknameGenerator implements DefaultNickname {

	private final List<String> adjectives;
	private final List<String> nouns;

	@Override
	public String generate() {
		Random random = ThreadLocalRandom.current();
		String adjective = adjectives.get(random.nextInt(adjectives.size()));
		String noun = nouns.get(random.nextInt(nouns.size()));
		return adjective + " " + noun;
	}
}
