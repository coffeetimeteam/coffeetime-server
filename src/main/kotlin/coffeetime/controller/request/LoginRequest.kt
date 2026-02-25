package coffeetime.controller.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class LoginRequest(
	@field:NotBlank(message = "이메일을 입력해주세요.")
	val username: String,
	@field:NotBlank(message = "비밀번호를 입력해주세요.")
	@field:Size(min = 8, max = 30, message = "비밀번호는 8자 이상, 30자 이하여야 합니다.")
	val password: String
)
