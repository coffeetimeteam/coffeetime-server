package coffeetime.dto;

import java.util.List;
import lombok.Builder;

@Builder
public record CoffeeFormResponse(
	List<String> locationType,
	List<String> coffeeType,
	List<String> sizeType,
	List<String> tasteType,
	List<String> priceType,
	List<Integer> coffeeScoreType,
	List<String> uploadStatusType
) {

}
