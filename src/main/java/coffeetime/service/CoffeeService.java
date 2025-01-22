package coffeetime.service;

import coffeetime.domain.Coffee;
import coffeetime.domain.Image;
import coffeetime.domain.User;
import coffeetime.domain.type.CoffeeType;
import coffeetime.domain.type.LocationType;
import coffeetime.domain.type.PriceType;
import coffeetime.domain.type.SizeType;
import coffeetime.domain.type.TasteType;
import coffeetime.dto.CoffeeCreateRequest;
import coffeetime.dto.CoffeeFormResponse;
import coffeetime.dto.CoffeeResponse;
import coffeetime.exception.CoffeeTimeException;
import coffeetime.exception.EntryPayloadCode;
import coffeetime.repository.CoffeeRepository;
import coffeetime.repository.ImageRepository;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class CoffeeService {

	@Value("${spring.server.url}")
	private String serverUrl;

	private final CoffeeRepository coffeeRepository;
	private final ImageRepository imageRepository;
	private final ImageService imageService;

	public CoffeeFormResponse getForm() {
		final List<String> locationList =
			Arrays.stream(LocationType.values())
				.map(LocationType::getLocation)
				.toList();
		final List<String> coffeeList = Arrays.stream(CoffeeType.values())
			.map(CoffeeType::getCoffee)
			.toList();
		final List<String> sizeList = Arrays.stream(SizeType.values())
			.map(SizeType::getSize)
			.toList();
		final List<String> tasteList = Arrays.stream(TasteType.values())
			.map(TasteType::getTaste)
			.toList();
		final List<String> priceList = Arrays.stream(PriceType.values()).map(PriceType::getPrice)
			.toList();
		final List<Integer> coffeeScoreList = List.of(1, 2, 3, 4, 5);
		return CoffeeFormResponse.builder()
			.locationType(locationList)
			.coffeeType(coffeeList)
			.sizeType(sizeList)
			.tasteType(tasteList)
			.priceType(priceList)
			.coffeeScoreType(coffeeScoreList)
			.build();
	}

	@Transactional
	public void createCoffee(User user, CoffeeCreateRequest request, List<MultipartFile> images) {
		final Coffee saveCoffee = Coffee.createCoffee(
			user,
			request.rememberDate(),
			request.rememberTime(),
			LocationType.fromDisplayName(request.location()),
			CoffeeType.fromDisplayName(request.coffee()),
			SizeType.fromDisplayName(request.size()),
			TasteType.fromDisplayName(request.taste()),
			PriceType.fromDisplayName(request.price()),
			request.coffeeScore(),
			null);
		final Coffee coffee = coffeeRepository.save(saveCoffee);
		if (coffee.getId() == null) {
			throw new CoffeeTimeException(EntryPayloadCode.FAIL_SAVE_COFFEE);
		}
		if (images != null && !images.isEmpty()) {
			final List<String> uploadedImages = imageService.uploadImages(images);
			imageRepository.saveAll(Image.saveImage(coffee, uploadedImages));
		}
	}

	@Transactional(readOnly = true)
	public List<CoffeeResponse> getCoffeesByDate(User user, LocalDate date) {
		LocalDate targetDate = date != null ? date : LocalDate.now();
		List<Coffee> coffees = coffeeRepository.findDailyCoffees(
			user, targetDate);

		return coffees.stream()
			.map(coffee -> {
				List<String> imageUrls = coffee.getImages().stream()
					.map(Image::getUrl)
					.map(key -> serverUrl + "/api/v1/images/" + key)
					.collect(Collectors.toList());
				return CoffeeResponse.of(coffee, imageUrls);
			})
			.collect(Collectors.toList());
	}
}
