package coffeetime.domain.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RoleType {
	GENERAL_USER,
	SPECIAL_USER,
	ADMIN;
}
