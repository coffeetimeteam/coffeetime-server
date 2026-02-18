package coffeetime.controller.response

import coffeetime.domain.CoffeeHabits
import coffeetime.domain.Feed
import coffeetime.domain.Member

data class FeedResponse(
    val nickname: String,
    val date: String,
    val coffeeCount: Int,
    val message: String,
    val coffeeHabits: CoffeeHabits,
    val image: String?,
    val imageCount: Long,
    val currentMessage: String,
    val encouragementMessage: String
) {
    companion object {

        fun of(member: Member, feed: Feed, messages: Map<String, String>): FeedResponse = FeedResponse(
            nickname = member.nickname,
            feed.date.toString(),
            feed.coffeeCount,
            "",
            CoffeeHabits.of(feed),
            feed.image,
            feed.imageCount,
            messages["currentMessage"].toString(),
            encouragementMessage = messages["encouragementMessage"].toString()
        )
    }
}