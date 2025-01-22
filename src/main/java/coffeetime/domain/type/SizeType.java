package coffeetime.domain.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SizeType {
	LARGE("큰 거"),
	MEDIUM("중간 거"),
	SMALL("작은 거"),
	ENOUGH("적당한 거");

	private final String size;

}
