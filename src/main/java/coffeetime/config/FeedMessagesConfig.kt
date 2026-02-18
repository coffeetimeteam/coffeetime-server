package coffeetime.config;

import coffeetime.domain.type.CoffeeType
import coffeetime.support.response.FeedMessagesGenerator
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class FeedMessagesConfig {

    @Bean
    fun feedMessages(): FeedMessagesGenerator {
//		TODO("메시지 추가")
        val currentMessages = mapOf(
            0 to mapOf(
                CoffeeType.COFFEE to listOf(
                    "CoffeeTime을 아직 안 가졌어요", "아직 CoffeeTime을 갖지 않았어요", "CoffeeTime을 가지지 않았어요",
                    "커피 한 잔도 안했어요",
                ),
                CoffeeType.NONE_COFFEE to listOf(
                    "CoffeeTime 시작 전이에요", "아직 CoffeeTime을 시작하지 않았어요!", "CoffeeTime 깜빡하셨나요?",
                    "음료를 한 잔도 안마셨어요"
                )
            ),
            1 to mapOf(
                CoffeeType.COFFEE to listOf(
                    "커피 한 잔을 마셨어요", "커피 한 잔을 즐겼어요", "커피 한 잔으로 CoffeeTime을 시작했어요", "커피 한 잔 짠!"
                ),
                CoffeeType.NONE_COFFEE to listOf(
                    "음료 한 잔으로 기분 전환했어요", "음료 한 잔으로 CoffeeTime을 시작했어요", "음료 한 잔을 즐겼어요"
                )
            ),
            2 to mapOf(
                CoffeeType.COFFEE to listOf(
                    "벌써 커피 두 잔!을 마셨어요", "커피 두 잔!을 마셨어요", "커피 두 잔!을 즐겼어요"
                ),
                CoffeeType.NONE_COFFEE to listOf(
                    "벌써 음료 두 잔!을 마셨어요", "음료 두 잔!을 마셨어요", "음료 두 잔!을 즐겼어요"
                )
            ),
            3 to mapOf(
                CoffeeType.COFFEE to listOf(
                    "커피 세 잔!으로 적정량을 달성했어요", "커피 세 잔!!을 마셨어요", "벌써 커피 세 잔!!을 즐겼어요",
                    "커피 세 잔 완료️ ☕️"
                ),
                CoffeeType.NONE_COFFEE to listOf(
                    "음료 세 잔!을 마셨어요", "다양한 음료를 즐기고 있어요", "음료 세 잔 완료 🥤", "오늘은 음료의 날!"
                )
            ),
            4 to mapOf(
                CoffeeType.COFFEE to listOf(
                    "오늘 CoffeeTime을 많이 즐겼어요!"
                ),
                CoffeeType.NONE_COFFEE to listOf(
                    "오늘 음료 기록이 많네요!"
                )
            )
        );

        val encouragementMessages = mapOf(
            0 to mapOf(
                CoffeeType.COFFEE to listOf(
                    "커피 한 잔은 어떠세요? ☕️", "오늘 첫 Coffee Time 시작해볼까요?", "첫 한 잔이 기다리고 있어요 👀",
                    "아직 카페인 OFF! ☕️", "가볍게 한 잔 어떠세요? ☕️"
                ),
                CoffeeType.NONE_COFFEE to listOf(
                    "어떤 음료로 하루를 시작해볼까요? 🥤", "오늘의 첫 음료를 기록해볼까요?", "가볍게 음료 한 잔 어떠세요?"
                )
            ),
            1 to mapOf(
                CoffeeType.COFFEE to listOf(
                    "커피 없인 못 살아 정말 못 살아~ 🎶", "에너지 충전 완! 🤩", "즐거운 CoffeeTime 🏝"
                ),
                CoffeeType.NONE_COFFEE to listOf(
                    "🏝리프레시 했어요", "다음 음료는 뭘 마실까요? 🤔", "계속해서 음료를 즐겨보세요! 🕺",
                )
            ),
            2 to mapOf(
                CoffeeType.COFFEE to listOf(
                    "커피로 피곤함을 내쫓아볼까요? 🥱", "오늘도 카페인과 함께 집중 🔥",
                ),
                CoffeeType.NONE_COFFEE to listOf(
                    "기분 전환 완! 🕺", "휴식은 필수! 🏝️", "기분 전환은 필수! 🍹"
                )
            ),
            3 to mapOf(
                CoffeeType.COFFEE to listOf(
                    "카페인 수혈하셨군요! 💉", "커피 러버이시군요? 🫢", "물도 드시고 있으시죠? 🥺",
                ),
                CoffeeType.NONE_COFFEE to listOf(
                    "다양한 음료를 즐겼어요!"
                )
            ),
            4 to mapOf(
                CoffeeType.COFFEE to listOf(
                    "내일은 카페인을 조금 줄여볼까요? 🤔", "카페인 폭발하는 중 🚀", "수분 보충도 잊지 마세요 💧", "피곤한 날이군요 🥺"
                ),
                CoffeeType.NONE_COFFEE to listOf(
                    "다양한 음료 선택! 🙌", "오늘 음료 마스터 😎",
                )
            )
        )
        return FeedMessagesGenerator(currentMessages, encouragementMessages)
    }
}
