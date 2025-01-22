package coffeetime.domain.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TasteType {
	SOUR("신맛나는"),
	NUTTY("고소한"),
	DELICIOUS("맛있는"),
	DONT_LIKE("취향이 아닌");

	private final String taste;


	public static TasteType fromDisplayName(String displayName) {
		for (TasteType type : TasteType.values()) {
			if (type.taste.equals(displayName)) {
				return type;
			}
		}
		throw new IllegalArgumentException("No enum constant for display name: " + displayName);
	}
}
