package coffeetime.dto;

import static lombok.AccessLevel.PRIVATE;

import coffeetime.domain.Member;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = PRIVATE)
public class UserCreateResponse {

	private final Long id;
	private final String username;

	public static UserCreateResponse from(final Member member) {
		return new UserCreateResponse(member.getId(), member.getUsername());
	}
}

