package coffeetime.support.response

import coffeetime.domain.DefaultNickname
import java.util.List
import java.util.Random
import java.util.concurrent.ThreadLocalRandom
import lombok.RequiredArgsConstructor

@RequiredArgsConstructor
class DefaultNicknameGenerator(
	private val adjectives: List<String>,
	private val nouns: List<String>
): DefaultNickname {

	override fun generate(): String {
		val random = ThreadLocalRandom.current()
		val adjective = adjectives[random.nextInt(adjectives.size)]
		val noun = nouns[random.nextInt(nouns.size)]
		return "$adjective $noun"
	}
}
