package coffeetime.domain.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SizeType {
	LARGE("큰"),
	MEDIUM("중간"),
	SMALL("작은");

	private final String size;

	public static SizeType fromDisplayName(String displayName) {
		for (SizeType type : SizeType.values()) {
			if (type.size.equals(displayName)) {
				return type;
			}
		}
		throw new IllegalArgumentException("No enum constant for display name: " + displayName);
	}
}
