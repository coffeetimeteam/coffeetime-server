package coffeetime.controller;

import coffeetime.config.SecurityRequiredOperation;
import coffeetime.domain.Member;
import coffeetime.dto.GlobalResponse;
import coffeetime.dto.UserCreateRequest;
import coffeetime.dto.UserResponse;
import coffeetime.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class MemberController {

	private final MemberService memberService;

	@PostMapping("/auth/signup")
	public ResponseEntity<GlobalResponse> createUser(
		@RequestBody @Valid final UserCreateRequest request
	) {
		final GlobalResponse response = memberService.createUser(request);
		return ResponseEntity.ok().body(response);
	}

	@GetMapping("/my")
	@SecurityRequiredOperation
	public ResponseEntity<UserResponse> getUserInfo() {
		final Member member = memberService.getCurrentUser();
		final UserResponse response = new UserResponse(member.getId(), member.getUsername(),
			member.getNickname(), member.getRole());
		return ResponseEntity.ok().body(response);
	}

//	@PatchMapping("/my/nickname")
//	public ResponseEntity<Void> updateUser(
//		@RequestBody @Valid final NicknameUpdateRequest request
//	) {
//		userService.updateNickname(request);
//		return ResponseEntity.ok().build();
//	}

//	@PatchMapping("/v1/password")
//	public ResponseEntity<Void> updatePassword (
//		@RequestBody @Valid final PasswordUpdateRequest request
//	){
//		userService.updatePassword(request);
//		return ResponseEntity.ok().build();
//	}
}
