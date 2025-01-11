package coffeetime.config;

import coffeetime.domain.DefaultNickname;
import coffeetime.infrastructure.DefaultNicknameGenerator;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DefaultNicknameConfig {

	@Bean
	public DefaultNickname defaultNickname() {
		List<String> adjectives = List.of(
			"나른한", "느긋한", "멋쟁이", "흥겨운", "춤추는", "행복한", "감상적인", "낭만적인", "열정적인", "유쾌한", "설레는", "훈훈한",
			"편안한", "신나는", "여유로운", "신기한", "신비한", "든든한", "만족스러운", "감탄하는", "불안한", "무서운", "답답한", "슬픈", "심심한", "지루한", "피곤한", "지친", "고마운", "벅찬", "차분한",
			"흐뭇한", "힘든", "그리운", "놀란", "멍한", "얄미운"
		);
		List<String> nouns = List.of(
			"에스프레소", "아메리카노", "롱블랙", "라떼", "카푸치노", "모카", "플랫화이트", "프라푸치노", "아포가토", "콜드브루"
		);
		return new DefaultNicknameGenerator(adjectives, nouns);
	}
}