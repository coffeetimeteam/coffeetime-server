package coffeetime.domain.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CoffeeType {
	COFFEE("coffee"),
	NONE_COFFEE("none-coffee");;

	private final String coffee;

	public static CoffeeType fromDisplayName(String displayName) {
		for (CoffeeType type : CoffeeType.values()) {
			if (type.coffee.equals(displayName)) {
				return type;
			}
		}
		throw new IllegalArgumentException("No enum constant for display name: " + displayName);
	}
}
