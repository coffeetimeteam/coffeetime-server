package coffeetime.domain;

import coffeetime.exception.CoffeeTimeException;
import coffeetime.exception.EntryPayloadCode;
import java.util.Arrays;

public enum Role {
	
	GENERAL_USER,
	SPECIAL_USER;

	public static Role getRoleType(final String roleType) {
		return Arrays.stream(values())
			.filter(role -> role.name().toUpperCase().equals(roleType))
			.findAny().orElseThrow(
				() -> new CoffeeTimeException(EntryPayloadCode.NOT_ENOUGH_PERMISSION));
	}
}
