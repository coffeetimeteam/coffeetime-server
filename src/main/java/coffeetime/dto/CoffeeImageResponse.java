package coffeetime.dto;

import java.util.List;

public record CoffeeImageResponse(
	List<String> imageUrls
) {


	public static CoffeeImageResponse getCoffeeImages(List<String> imageUrls) {
		return new CoffeeImageResponse(imageUrls);
	}
}
