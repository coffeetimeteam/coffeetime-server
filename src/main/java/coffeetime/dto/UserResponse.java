package coffeetime.dto;

import coffeetime.domain.Role;

public record UserResponse (
	Long id,
	String username,
	String nickname,
	Role role
){
}
