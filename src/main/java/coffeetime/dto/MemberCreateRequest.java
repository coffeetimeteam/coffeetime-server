package coffeetime.dto;

import static lombok.AccessLevel.PROTECTED;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = PROTECTED)
public class MemberCreateRequest {

	@NotNull(message = "이메일을 입력해주세요.")
	private String username;

	@NotNull(message = "비밀번호를 입력해주세요.")
	@Size(min = 8, max = 30, message = "비밀번호는 8자 이상, 30자 이하여야 합니다.")
	private String password;

	@NotNull(message = "비밀번호 확인을 입력해주세요.")
	private String confirmPassword;

	public MemberCreateRequest(String username, String password, String confirmPassword) {
		this.username = username;
		this.password = password;
		this.confirmPassword = confirmPassword;
	}
}
