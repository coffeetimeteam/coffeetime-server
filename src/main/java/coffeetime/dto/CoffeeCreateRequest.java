package coffeetime.dto;

import static java.util.Collections.emptyList;
import static java.util.Objects.requireNonNullElse;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public record CoffeeCreateRequest(
	@NotNull(message = "날짜를 입력해주세요.")
	LocalDate rememberDate,

	@NotNull(message = "시간을 입력해주세요.")
	@JsonDeserialize(using = LocalTimeDeserializer.class)
	@JsonFormat(pattern = "HH:mm")
	LocalTime rememberTime,

	@NotNull(message = "장소를 선택해주세요.")
	String location,

	@NotNull(message = "커피 종류를 선택해주세요")
	String coffee,

	@NotNull(message = "사이즈를 선택해주세요.")
	String size,

	@NotNull(message = "맛을 평가해주세요.")
	String taste,

	@NotNull(message = "가격을 평가해주세요.")
	String price,

	@NotNull(message = "점수를 입력해주세요.")
	@Min(value = 1, message = "점수는 1점 이상이어야 합니다.")
	@Max(value = 5, message = "점수는 5점 이하여야 합니다.")
	int coffeeScore,

	@Size(max = 5, message = "이미지는 5장 이하로 가능합니다.")
	List<MultipartFile> images
) {

	public List<MultipartFile> images() {
		return requireNonNullElse(images, emptyList());
	}
}
