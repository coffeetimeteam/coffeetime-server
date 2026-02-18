package coffeetime.controller

import coffeetime.controller.response.FeedResponse
import coffeetime.domain.FeedService
import coffeetime.domain.MemberService
import coffeetime.support.response.FeedMessagesGenerator
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/feed")
class FeedController(
    private val memberService: MemberService,
    private val feedService: FeedService,
    private val feedMessagesGenerator: FeedMessagesGenerator,
) {
    @GetMapping
    fun getFeed(
        @RequestHeader(HttpHeaders.AUTHORIZATION) token: String,
    ): ResponseEntity<FeedResponse> {
        val member = memberService.getCurrentMember(token);
        val feed = feedService.getFeed(member)
        val messages = feedMessagesGenerator.getMessages(feed.coffeeCount, feed.favoriteCoffee)
        return ResponseEntity.ok().body(FeedResponse.of(member, feed, messages))
    }
}