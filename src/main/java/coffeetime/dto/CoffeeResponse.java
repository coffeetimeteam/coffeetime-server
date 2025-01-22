package coffeetime.dto;

import coffeetime.domain.Coffee;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public record CoffeeResponse(
	Long id,
	LocalDate rememberDate,
	LocalTime rememberTime,
	String locationType,
	String coffeeType,
	String sizeType,
	String tasteType,
	String priceType,
	Integer coffeeScore,
	List<String> imageKeys
) {

	public static CoffeeResponse of(Coffee coffee, List<String> imageKeys) {
		return new CoffeeResponse(
			coffee.getId(),
			coffee.getRememberDate(),
			coffee.getRememberTime(),
			coffee.getLocationType().getLocation(),
			coffee.getCoffeeType().getCoffee(),
			coffee.getSizeType().getSize(),
			coffee.getTasteType().getTaste(),
			coffee.getPriceType().getPrice(),
			coffee.getCoffeeScore(),
			imageKeys
		);
	}
} 