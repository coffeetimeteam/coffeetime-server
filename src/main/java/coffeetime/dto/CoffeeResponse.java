package coffeetime.dto;

import coffeetime.domain.Coffee;
import coffeetime.domain.Image;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

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
	List<String> imageUrls
) {


	public static CoffeeResponse of(Coffee coffee, List<String> imageUrls) {
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
			imageUrls
		);
	}


	public static List<CoffeeResponse> groupByMonth(List<Coffee> coffees) {
		return coffees.stream()
			.map(coffee -> of(coffee, coffee.getImages().stream()
				.map(Image::getUrl)
				.collect(Collectors.toList())))
			.collect(Collectors.toList());
	}
}