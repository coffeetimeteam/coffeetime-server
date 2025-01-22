package coffeetime.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import coffeetime.domain.Coffee;
import coffeetime.domain.User;
import coffeetime.domain.type.CoffeeType;
import coffeetime.domain.type.LocationType;
import coffeetime.domain.type.PriceType;
import coffeetime.domain.type.SizeType;
import coffeetime.domain.type.TasteType;
import coffeetime.dto.CoffeeCreateRequest;
import coffeetime.dto.CoffeeResponse;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class CoffeeServiceTests {

	@Value("${spring.server.url}")
	private String serverUrl;

	@Autowired
	private CoffeeService coffeeService;

	@Autowired
	private UserService userService;

	@MockBean
	private ImageService imageService;

	private User testUser;
	private CoffeeCreateRequest testRequest;

	@BeforeEach
	void setUp() {
		testUser = userService.getCurrentUser();
		testRequest = new CoffeeCreateRequest(
			LocalDate.now(),
			LocalTime.now(),
			"회사",
			"coffee",
			"중간 거",
			"신맛나는",
			"가성비 있는",
			5
		);
	}

	@Test
	public void testCreateCoffeeWithImage() {
		// given
		MockMultipartFile testImage = new MockMultipartFile(
			"image",
			"test.jpg",
			"image/jpeg",
			"test image content".getBytes()
		);

		when(imageService.uploadImages(any()))
			.thenReturn(List.of("test-image-key"));

		// when
		coffeeService.createCoffee(testUser, testRequest, List.of(testImage));

		// then
		List<CoffeeResponse> responses = coffeeService.getCoffeesByDate(testUser, LocalDate.now());
		assertThat(responses).isNotEmpty();

		CoffeeResponse response = responses.get(0);
		assertThat(response.imageKeys()).isNotEmpty();
		assertThat(response.imageKeys().get(0))
			.startsWith(serverUrl + "/api/v1/images/");
	}

	@Test
	public void testGetCoffeesByDate() {
		// given
		Coffee coffee = Coffee.createCoffee(
			testUser,
			LocalDate.now(),
			LocalTime.now(),
			LocationType.HOME,
			CoffeeType.COFFEE,
			SizeType.MEDIUM,
			TasteType.DELICIOUS,
			PriceType.AVERAGE,
			5,
			null
		);

		// when
		List<CoffeeResponse> responses = coffeeService.getCoffeesByDate(testUser, LocalDate.now());

		// then
		assertThat(responses).isNotNull();
		if (!responses.isEmpty()) {
			responses.forEach(response -> {
				assertThat(response.id()).isNotNull();
				assertThat(response.rememberDate()).isNotNull();
				assertThat(response.rememberTime()).isNotNull();
				response.imageKeys().forEach(imageUrl -> {
					assertThat(imageUrl).startsWith(serverUrl + "/api/v1/images/");
				});
			});
		}
	}

	@Test
	public void testCreateCoffeeWithLocation() {
		// given
		testRequest = new CoffeeCreateRequest(
			LocalDate.now(),
			LocalTime.now(),
			"집",
			"coffee",
			"큰 거",
			"신맛나는",
			"가성비 있는",
			5
		);

		// when
		coffeeService.createCoffee(testUser, testRequest, List.of());

		// then
		List<CoffeeResponse> responses = coffeeService.getCoffeesByDate(testUser, LocalDate.now());
		assertThat(responses).isNotEmpty();

		CoffeeResponse response = responses.get(0);
		assertThat(response.locationType()).isEqualTo(
			LocationType.HOME.getLocation()); // DB에 저장된 location 확인
	}
} 