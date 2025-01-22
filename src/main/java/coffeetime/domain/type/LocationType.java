package coffeetime.domain.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum LocationType {
	HOME("집"),
	OFFICE("회사"),
	FRANCHISE("프랜차이즈"),
	LOCAL_CAFE("개인카페");

	private final String location;

	public static LocationType fromDisplayName(String displayName) {
		for (LocationType type : LocationType.values()) {
			if (type.location.equals(displayName)) {
				return type;
			}
		}
		throw new IllegalArgumentException("No enum constant for display name: " + displayName);
	}
}
