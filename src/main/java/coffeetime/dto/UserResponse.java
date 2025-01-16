package coffeetime.dto;

import coffeetime.domain.type.RoleType;

public record UserResponse(
	Long id,
	String username,
	String nickname,
	RoleType roleType
) {

}
