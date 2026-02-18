package coffeetime.domain

import coffeetime.domain.type.*
import java.time.LocalDate

data class Feed(
    val date: LocalDate,
    val coffeeCount: Int,
    val favoriteLocation: LocationType? = null,
    val favoriteSize: SizeType? = null,
    val favoriteCoffee: CoffeeType? = null,
    val favoriteTaste: TasteType? = null,
    val favoritePrice: PriceType? = null,
    val avgCoffeeScore: Long = 0,
    val image: String? = null,
    val imageCount: Long
)
