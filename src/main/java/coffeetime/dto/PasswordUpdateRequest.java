package coffeetime.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record PasswordUpdateRequest(
	@NotNull(message = "비밀번호를 입력해주세요.")
	@Size(min = 8, max = 30, message = "비밀번호는 8자 이상, 30자 이하여야 합니다.")
	String currentPassword,

	@NotNull(message = "비밀번호를 입력해주세요.")
	@Size(min = 8, max = 30, message = "비밀번호는 8자 이상, 30자 이하여야 합니다.")
	String newPassword
) {

}
