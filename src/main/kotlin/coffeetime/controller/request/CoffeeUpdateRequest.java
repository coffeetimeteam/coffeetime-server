package coffeetime.controller.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public record CoffeeUpdateRequest(
	@NotNull(message = "수정할 날짜를 입력해주세요.")
	LocalDate rememberDate,

	@NotNull(message = "수정할 시간을 입력해주세요.")
	@JsonDeserialize(using = LocalTimeDeserializer.class)
	@JsonFormat(pattern = "HH:mm")
	LocalTime rememberTime,

	@NotNull(message = "수정할 장소를 선택해주세요.")
	String location,

	@NotNull(message = "수정할 커피 종류를 선택해주세요")
	String coffee,

	@NotNull(message = "수정할 사이즈를 선택해주세요.")
	String size,

	@NotNull(message = "수정할 맛을 선택해주세요..")
	String taste,

	@NotNull(message = "수정할 가격을 입력해주세요.")
	String price,

	@NotNull(message = "수정할 점수를 입력해주세요.")
	@Min(value = 1, message = "점수는 1점 이상이어야 합니다.")
	@Max(value = 5, message = "점수는 5점 이하여야 합니다.")
	int coffeeScore,

	List<String> imageUrls,

	List<MultipartFile> images
) {

}
