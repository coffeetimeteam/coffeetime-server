package coffeetime.service;

import static java.util.stream.Collectors.toList;

import coffeetime.domain.Coffee;
import coffeetime.domain.Image;
import coffeetime.domain.Member;
import coffeetime.domain.type.CoffeeType;
import coffeetime.domain.type.LocationType;
import coffeetime.domain.type.PriceType;
import coffeetime.domain.type.SizeType;
import coffeetime.domain.type.TasteType;
import coffeetime.dto.CoffeeCreateRequest;
import coffeetime.dto.CoffeeFormResponse;
import coffeetime.dto.CoffeeResponse;
import coffeetime.dto.CoffeeUpdateRequest;
import coffeetime.dto.GlobalResponse;
import coffeetime.exception.CoffeeTimeException;
import coffeetime.exception.EntryPayloadCode;
import coffeetime.repository.CoffeeRepository;
import coffeetime.repository.ImageRepository;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class CoffeeService {

	private final CoffeeRepository coffeeRepository;
	private final ImageRepository imageRepository;
	private final ImageService imageService;
	private final MemberService memberService;

	@Transactional
	public GlobalResponse createCoffee(final CoffeeCreateRequest request, final String token) {
		final Member member = memberService.getCurrentMember(token);
		final Coffee saveCoffee = Coffee.create(
			member,
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
		uploadImageFiles(coffee, request.images());
		return new GlobalResponse(EntryPayloadCode.SUCCESS_CREATED);
	}

	@Transactional(readOnly = true)
	public CoffeeFormResponse getForm(final String token) {
		final Member member = memberService.getCurrentMember(token);
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
	public CoffeeResponse findCoffeeId(final Long coffeeId, final String token) {
		final Member member = memberService.getCurrentMember(token);
		final Coffee coffee = coffeeRepository.findByIdAndMember(coffeeId, member)
			.orElseThrow(() -> new CoffeeTimeException(EntryPayloadCode.NOT_FOUND_COFFEE));
		return CoffeeResponse.of(coffee, coffee.getImages().stream().map(Image::getUrl).toList());
	}

	@Transactional(readOnly = true)
	public List<Map<String, Object>> findCoffeesByMonth(final Integer year,
		final Integer month, final String token) {
		final Member member = memberService.getCurrentMember(token);
		final LocalDate targetDate = (year == null || month == null) ? LocalDate.now() :
			LocalDate.of(year, month, 1);
		final List<Coffee> coffees = coffeeRepository.findCoffeesByMonth(member,
			targetDate.getYear(),
			targetDate.getMonthValue());
		final List<CoffeeResponse> coffeeResponses = CoffeeResponse.groupByMonth(coffees);
		return coffeeResponses.stream()
			.collect(Collectors.groupingBy(
				coffee -> coffee.rememberDate().toString(),
				Collectors.mapping(coffee -> Map.of(
					"id", coffee.id(),
					"rememberDate", coffee.rememberDate().toString(),
					"rememberTime", coffee.rememberTime().toString(),
					"locationType", coffee.locationType(),
					"coffeeType", coffee.coffeeType(),
					"sizeType", coffee.sizeType(),
					"tasteType", coffee.tasteType(),
					"priceType", coffee.priceType(),
					"coffeeScore", coffee.coffeeScore(),
					"imageUrls", coffee.imageUrls()
				), toList())
			))
			.entrySet().stream()
			.map(entry -> Map.of(
				"date", entry.getKey(),
				"items", entry.getValue()
			))
			.toList();
	}

	@Transactional(readOnly = true)
	public List<CoffeeResponse> findCoffeesByDate(final LocalDate date, final String token) {
		final LocalDate targetDate = date != null ? date : LocalDate.now();
		final Member member = memberService.getCurrentMember(token);
		final List<Coffee> coffees = coffeeRepository.findCoffeesByDate(
			member, targetDate);
		return coffees.stream()
			.map(coffee -> {
				List<String> imageUrls = coffee.getImages().stream()
					.map(Image::getUrl)
					.toList();
				return CoffeeResponse.of(coffee, imageUrls);
			})
			.toList();
	}

	@Transactional
	public void updateCoffee(final Long coffeeId, final CoffeeUpdateRequest request,
		final String token) {
		final Member member = memberService.getCurrentMember(token);
		final Coffee coffee = coffeeRepository.findByIdAndMember(coffeeId, member)
			.orElseThrow(() -> new CoffeeTimeException(EntryPayloadCode.NOT_FOUND_COFFEE));
		final List<String> currentImageUrls = coffee.getImages().stream()
			.map(Image::getUrl)
			.toList();
		final List<String> requestImageUrls = request.imageUrls();
		final List<String> imagesToDelete = currentImageUrls.stream()
			.filter(url -> !requestImageUrls.contains(url))
			.toList();
		uploadImageFiles(coffee, request.images());
		deleteImages(imagesToDelete);
	}

	private void uploadImageFiles(final Coffee coffee, final List<MultipartFile> images) {
		if (images.stream().anyMatch(MultipartFile::isEmpty)) {
			return;
		}
		final List<String> uploadedImages = imageService.uploadImages(images);
		imageRepository.saveAll(Image.saveImage(coffee, uploadedImages));
	}

	private void deleteImages(final List<String> imageUrls) {
		if (imageUrls.isEmpty()) {
			return;
		}
		System.out.println("delete imageUrs>>>" + imageUrls);
		imageService.deleteImages(imageUrls);
		imageRepository.deleteByUrls(imageUrls);
	}

	@Transactional
	public void deleteCoffee(final Long coffeeId, final String token) {
		final Member member = memberService.getCurrentMember(token);
		final Coffee coffee = coffeeRepository.findByIdAndMember(coffeeId, member)
			.orElseThrow(() -> new CoffeeTimeException(EntryPayloadCode.NOT_FOUND_COFFEE));
		try {
			coffeeRepository.deleteById(coffee.getId());
			imageRepository.deleteByCoffee(coffee);
		} catch (Exception e) {
			throw new CoffeeTimeException(EntryPayloadCode.FAIL_IMAGE_DELETE);
		}
	}
}
