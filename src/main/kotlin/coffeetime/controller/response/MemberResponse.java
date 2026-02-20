package coffeetime.controller.response;

import coffeetime.domain.type.RoleType;
import java.util.UUID;

public record MemberResponse(
	UUID id,
	String username,
	String nickname,
	RoleType role
) {

}
