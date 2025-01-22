package coffeetime.domain.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PriceType {
	CHEAP("가성비 있는"),
	AVERAGE("적당한"),
	EXPENSIVE("비싼");

	private final String price;

}
