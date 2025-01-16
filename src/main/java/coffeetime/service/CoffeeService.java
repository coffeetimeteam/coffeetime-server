package coffeetime.service;

import coffeetime.domain.type.CoffeeScoreType;
import coffeetime.domain.type.CoffeeType;
import coffeetime.domain.type.LocationType;
import coffeetime.domain.type.PriceType;
import coffeetime.domain.type.SizeType;
import coffeetime.domain.type.TasteType;
import coffeetime.domain.type.UploadStatusType;
import coffeetime.dto.CoffeeFormResponse;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CoffeeService {

	public CoffeeFormResponse getForm() {
		List<String> locationList =
			Arrays.stream(LocationType.values())
				.map(LocationType::getLocation)
				.toList();

		List<String> coffeeList = Arrays.stream(CoffeeType.values())
			.map(CoffeeType::getCoffee)
			.toList();

		List<String> sizeList = Arrays.stream(SizeType.values())
			.map(SizeType::getSize)
			.toList();

		List<String> tasteList = Arrays.stream(TasteType.values())
			.map(TasteType::getTaste)
			.toList();

		List<String> priceList = Arrays.stream(PriceType.values()).map(PriceType::getPrice)
			.toList();

		List<Integer> coffeeScoreList =
			Arrays.stream(CoffeeScoreType.values()).map(CoffeeScoreType::getCoffeeScore).toList();

		List<String> uploadStatusList =
			Arrays.stream(UploadStatusType.values()).map(UploadStatusType::getUploadStatus)
				.toList();

		return CoffeeFormResponse.builder()
			.locationType(locationList)
			.coffeeType(coffeeList)
			.sizeType(sizeList)
			.tasteType(tasteList)
			.priceType(priceList)
			.coffeeScoreType(coffeeScoreList)
			.uploadStatusType(uploadStatusList)
			.build();
	}
}
