package coffeetime.domain.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PriceType {
	CHEAP("가성비 있는"),
	AVERAGE("적당한"),
	EXPENSIVE("비싼");

	private final String price;

	public static PriceType fromDisplayName(String displayName) {
		for (PriceType type : PriceType.values()) {
			if (type.price.equals(displayName)) {
				return type;
			}
		}
		throw new IllegalArgumentException("No enum constant for display name: " + displayName);
	}
}
