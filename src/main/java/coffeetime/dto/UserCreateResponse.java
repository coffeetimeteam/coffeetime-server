package coffeetime.dto;

import static lombok.AccessLevel.PRIVATE;

import coffeetime.domain.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = PRIVATE)
public class UserCreateResponse {

	private final Long id;
	private final String username;

	public static UserCreateResponse from(final User user) {
		return new UserCreateResponse(user.getId(), user.getUsername());
	}
}

