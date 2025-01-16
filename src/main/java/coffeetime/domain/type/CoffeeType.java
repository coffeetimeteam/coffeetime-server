package coffeetime.domain.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CoffeeType {
	COFFEE("coffee"),
	NONE_COFFEE("none-coffee");;

	private final String coffee;
}
