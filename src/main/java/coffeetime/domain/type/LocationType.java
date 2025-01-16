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
}
