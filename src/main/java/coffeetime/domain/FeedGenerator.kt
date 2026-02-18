package coffeetime.domain

import java.time.LocalDate

object FeedGenerator {

    fun generate(coffees: List<Coffee>, date: LocalDate): Feed {
        return Feed(
            date,
            coffees.size,
            findMostFrequent(coffees) { it.locationType },
            findMostFrequent(coffees) { it.sizeType },
            findMostFrequent(coffees) { it.coffeeType },
            findMostFrequent(coffees) { it.tasteType },
            findMostFrequent(coffees) { it.priceType },
            calculateAverage(coffees) { it.coffeeScore },
            findLastImageUrl(coffees),
            calculateTotalImageCount(coffees)
        )
    }

    private fun <T> findMostFrequent(coffees: List<Coffee>, selector: (Coffee) -> T?): T? {
        return coffees
            .mapNotNull(selector)
            .groupingBy { it }
            .eachCount()
            .maxByOrNull { it.value }
            ?.key
    }

    private fun calculateAverage(coffees: List<Coffee>, selector: (Coffee) -> Int?): Long {
        return coffees
            .mapNotNull(selector)
            .map { it }
            .average()
            .takeIf { !it.isNaN() }
            ?.toLong()
            ?: 0
    }

    private fun findLastImageUrl(coffees: List<Coffee>): String? {
        return coffees
            .flatMap { it.images }
            .last { it.url != null }
            .url
    }

    private fun calculateTotalImageCount(coffees: List<Coffee>): Long {
        return coffees
            .flatMap { it.images }
            .count { it.url != null }
            .toLong()
    }
}