package coffeetime.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import coffeetime.controller.request.CoffeeCreateRequest;
import coffeetime.controller.response.CoffeeResponse;
import coffeetime.controller.response.GlobalResponse;
import coffeetime.domain.Coffee;
import coffeetime.domain.CoffeeService;
import coffeetime.domain.ImageService;
import coffeetime.domain.Member;
import coffeetime.domain.MemberService;
import coffeetime.domain.type.CoffeeType;
import coffeetime.domain.type.LocationType;
import coffeetime.domain.type.PriceType;
import coffeetime.domain.type.SizeType;
import coffeetime.domain.type.TasteType;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@SpringBootTest
@Transactional
public class CoffeeServiceTests {

	@Autowired
	private CoffeeService coffeeService;

	@Autowired
	private MemberService memberService;

	@MockBean
	private ImageService imageService;

	private Member testMember;
	private CoffeeCreateRequest testRequest;

	@BeforeEach
	void setUp() {
		testMember = memberService.getCurrentUser();
	}

	@Test
	public void testCreateCoffeeWithImage() {
		MockMultipartFile testImage = new MockMultipartFile(
			"image",
			"test.jpg",
			"image/jpeg",
			"test image content".getBytes()
		);
		testRequest = new CoffeeCreateRequest(
			LocalDate.now(),
			LocalTime.now(),
			"회사",
			"coffee",
			"중간 거",
			"신맛나는",
			"가성비 있는",
			5,
			(List<MultipartFile>) testImage
		);

		// when
		when(imageService.uploadImages(any()))
			.thenReturn(List.of("test-image-key"));
		GlobalResponse response = coffeeService.createCoffee(testRequest);

		// then
		assertThat(response.getStatus()).isEqualTo(200);
	}

	@Test
	public void testGetCoffeesByDate() {
		// given
		Coffee coffee = Coffee.create(
			testMember,
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
		List<CoffeeResponse> responses = coffeeService.findCoffeesByDate(LocalDate.now());

		// then
		assertThat(responses).isNotNull();
		if (!responses.isEmpty()) {
			responses.forEach(response -> {
				assertThat(response.id()).isNotNull();
				assertThat(response.rememberDate()).isNotNull();
				assertThat(response.rememberTime()).isNotNull();
			});
		}
	}
}