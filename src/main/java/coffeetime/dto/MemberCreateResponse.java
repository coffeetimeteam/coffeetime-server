package coffeetime.dto;

import static lombok.AccessLevel.PRIVATE;

import coffeetime.domain.Member;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = PRIVATE)
public class MemberCreateResponse {

	private final Long id;
	private final String username;

	public static MemberCreateResponse from(final Member member) {
		return new MemberCreateResponse(member.getId(), member.getUsername());
	}
}

