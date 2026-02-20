package coffeetime.controller.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record NicknameUpdateRequest(
	@NotNull(message = "닉네임을 입력해주세요.")
	@Size(min = 2, max = 20, message = "닉네임은 2글자 이상 20글자 미만입니다.")
	String nickname
) {

}
