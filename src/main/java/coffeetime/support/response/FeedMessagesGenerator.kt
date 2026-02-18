package coffeetime.support.response;

import coffeetime.domain.type.CoffeeType
import java.util.concurrent.ThreadLocalRandom

class FeedMessagesGenerator(
    private val currentMessages: Map<Int, Map<CoffeeType, List<String>>>,
    private val encouragementMessages: Map<Int, Map<CoffeeType, List<String>>>
) {
    fun getMessages(count: Int, coffeeType: CoffeeType?): Map<String, String> {
        val resolvedCoffeType = coffeeType ?: listOf(CoffeeType.COFFEE, CoffeeType.NONE_COFFEE).random()
        return mapOf(
            "currentMessage" to getCurrentMessage(count, resolvedCoffeType),
            "encouragementMessage" to getEncouragementMessage(count, resolvedCoffeType)
        )
    }

    private fun getCurrentMessage(count: Int, coffeeType: CoffeeType): String {
        val messages = currentMessages.getValue(count).getValue(coffeeType)
        val random = ThreadLocalRandom.current()
        return messages[random.nextInt(messages.size)]
    }

    private fun getEncouragementMessage(count: Int, coffeeType: CoffeeType): String {
        val messages = encouragementMessages.getValue(count).getValue(coffeeType)
        val random = ThreadLocalRandom.current()
        return messages[random.nextInt(messages.size)]
    }
}