package coffeetime.service

import coffeetime.domain.Coffee
import coffeetime.domain.FeedGenerator
import coffeetime.domain.Image
import coffeetime.domain.type.*
import coffeetime.repository.CoffeeRepository
import coffeetime.repository.ImageRepository
import coffeetime.repository.MemberEntity
import coffeetime.repository.MemberRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.time.LocalTime

@SpringBootTest
@Transactional
class FeedServiceTests(
    private val memberRepository: MemberRepository,
    private val coffeeRepository: CoffeeRepository,
    private val imageRepository: ImageRepository,
) {

    @Test
    fun `조회 성공 시 피드를 가져온다`() {
        // given
        val member = MemberEntity(
            "tester@test.com",
            LoginType.EMAIL,
            "tester",
            "password",
            RoleType.GENERAL_USER
        )

        memberRepository.save(member)

        val today = LocalDate.now()

        val coffeeAt10 = Coffee.create(
            member.id,
            today,
            LocalTime.of(10, 0),
            LocationType.HOME,
            CoffeeType.COFFEE,
            SizeType.SMALL,
            TasteType.DELICIOUS,
            PriceType.CHEAP,
            4,
            emptyList()
        )
        coffeeRepository.save(coffeeAt10)

        val coffeeAt11 = Coffee.create(
            member.id,
            today,
            LocalTime.of(11, 0),
            LocationType.HOME,
            CoffeeType.COFFEE,
            SizeType.SMALL,
            TasteType.DELICIOUS,
            PriceType.CHEAP,
            5,
            emptyList()
        )
        coffeeRepository.save(coffeeAt11)
        imageRepository.saveAll(
            Image.saveImage(coffeeAt11, listOf("https://image-test.com/test.jpg"))
        )

        // when
        val now = LocalDate.now()
        val coffees = coffeeRepository.findCoffeesByDate(member.id, now)
        val feed = FeedGenerator.generate(coffees, now)

        assertThat(feed.coffeeCount).isEqualTo(2)
        assertThat(feed.favoriteLocation).isEqualTo(LocationType.HOME)
        assertThat(feed.favoriteSize).isEqualTo(SizeType.SMALL)
        assertThat(feed.favoriteCoffee).isEqualTo(CoffeeType.COFFEE)
        assertThat(feed.favoriteTaste).isEqualTo(TasteType.DELICIOUS)
        assertThat(feed.favoritePrice).isEqualTo(PriceType.CHEAP)
        assertThat(feed.avgCoffeeScore).isEqualTo(4)
        assertThat(feed.imageCount).isEqualTo(1L)
        assertThat(feed.image).isEqualTo("https://image-test.com/test.jpg")
    }
}
