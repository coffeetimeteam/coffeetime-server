package coffeetime.config;

import coffeetime.domain.FeedMessages;
import coffeetime.domain.type.CoffeeType;
import coffeetime.support.response.FeedMessagesGenerator;
import java.util.List;
import java.util.Map;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeedMessagesConfig {

	@Bean
	public FeedMessages feedMessages() {
		final Map<Integer, Map<CoffeeType, List<String>>> currentMessages = Map.of(
			0, Map.of(
				CoffeeType.COFFEE, List.of(
					"Coffee Time을 아직 안 가졌어요", "아직 Coffee Time을 갖지 않았어요",
					"Coffee Time을 가지지 않았어요", "한 잔도 마시지 않았어요! 🫢",
					"Coffee Time을 깜빡했나요? 🥺"
				)
			),
			1, Map.of(
				CoffeeType.COFFEE, List.of(
					"커피 한 잔을 마셨어요", "커피 한 잔을 즐겼어요!", "커피 한 잔으로 Coffee Time을 시작했어요",
					"커피 한 잔으로 기분 전환했어요!"
				),
				CoffeeType.NONE_COFFEE, List.of(
					"음료 한 잔으로 기분 전환했어요", "음료 한 잔으로 Coffee Time을 시작했어요!",
					"음료 한 잔을 즐겼어요!"
				)
			),
			2, Map.of(
				CoffeeType.COFFEE, List.of(
					"벌써 커피 두 잔!을 마셨어요", "커피 두 잔을 마셨어요!", "커피 두 잔을 즐겼어요!",
					"적당한 커피 두 잔을 마셨어요!"
				),
				CoffeeType.NONE_COFFEE, List.of(
					"음료 두 잔!을 마셨어요", "음료 두 잔을 즐겼어요!", "음료 두 잔!을 마셨어요"
				)
			),
			3, Map.of(
				CoffeeType.COFFEE, List.of(
					"커피 세 잔으로 적정량을 달성했어요 🥺", "커피 세 잔을 마셨어요!", "커피 세 잔을 즐겼어요!"
				)
			),
			4, Map.of(
				CoffeeType.COFFEE, List.of(
					"커피 네 잔을 마셨어요!", ""
				)
			)
		);
		final Map<Integer, Map<CoffeeType, List<String>>> encouragementMessages = Map.of(
			0, Map.of(
				CoffeeType.COFFEE, List.of(
					"어떤 음료로 하루를 시작해볼까요? 🥤", "커피 한 잔은 어떠세요? ☕️", "바쁜 하루이시군요 🥺"
				)
			),
			1, Map.of(
				CoffeeType.COFFEE, List.of(
					"🎵 커피 없인 못 살아 정말 못 살아~ 🎶", "에너지 충전! 덜 피곤한 기분 🤩",
					"즐거운 Coffee Time 🏝️"
				),
				CoffeeType.NONE_COFFEE, List.of(
					"커피 없는 날도 좋죠!", "세상엔 맛있는 음료가 많죠!", "두 번쨰 음료는 무얼 마실까요? 🤔",
					"계속해서 음료를 즐겨보세요! 🕺", "커피 없이도 하루를 버티는 정신력, 멋져요! 👍",
					"자연 에너지 100%! 체력이 부러워요 🥲", "카페인 없이도 맑은 정신, 대단해요! 👍"
				)
			),
			2, Map.of(
				CoffeeType.COFFEE, List.of(
					"커피로 피곤함을 내쫓아볼까요? 🥱", "집중력이 "
				),
				CoffeeType.NONE_COFFEE, List.of(
					"휴식은 필수! 🏝️", "기분 전환은 필수! 🍹"
				)
			),
			3, Map.of(
				CoffeeType.COFFEE, List.of(
					""
				),
				CoffeeType.NONE_COFFEE, List.of(
					"다양한 음료를 즐기셨군요!"
				)
			),
			4, Map.of(
				CoffeeType.COFFEE, List.of(
					"내일은 조금 더 절제된 Coffee Time을 가져볼까요? ☕️"
				),
				CoffeeType.NONE_COFFEE, List.of(
					""
				)
			)
		);
		return new FeedMessagesGenerator(currentMessages, encouragementMessages);
	}
}
