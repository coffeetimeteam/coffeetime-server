package coffeetime.domain

import coffeetime.repository.CoffeeRepository
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@Component
class FeedService(
    private val coffeeRepository: CoffeeRepository,
) {
    @Transactional(readOnly = true)
    fun getFeed(member: Member): Feed {
        val now = LocalDate.now()
        val coffees = coffeeRepository.findCoffeesByDate(member.id, now)
        return FeedGenerator.generate(coffees, now)
    }
}