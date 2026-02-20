package coffeetime.domain

import coffeetime.domain.type.*

data class CoffeeHabits(
    val favoriteLocation: LocationType? = null,
    val favoriteSize: SizeType? = null,
    val favoriteCoffee: CoffeeType? = null,
    val favoriteTaste: TasteType? = null,
    val favoritePrice: PriceType? = null,
    val avgCoffeeScore: Long? = null,
) {
    companion object {
        fun of(feed: Feed): CoffeeHabits = CoffeeHabits(
            feed.favoriteLocation,
            feed.favoriteSize,
            feed.favoriteCoffee,
            feed.favoriteTaste,
            feed.favoritePrice,
            feed.avgCoffeeScore
        )
    }
}
