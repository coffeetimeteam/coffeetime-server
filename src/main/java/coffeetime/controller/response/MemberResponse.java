package coffeetime.controller.response;

import coffeetime.domain.type.RoleType;

public record MemberResponse(
	Long id,
	String username,
	String nickname,
	RoleType role
) {

}
