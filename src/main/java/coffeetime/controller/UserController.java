package coffeetime.controller;

import coffeetime.dto.APIResponse;
import coffeetime.dto.NicknameUpdateRequest;
import coffeetime.dto.UserCreateRequest;
import coffeetime.dto.UserResponse;
import coffeetime.exception.EntryPayloadCode;
import coffeetime.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class UserController {

	private final UserService userService;

	@PostMapping("/auth/signup")
	public ResponseEntity<APIResponse> createUser(
		@RequestBody @Valid final UserCreateRequest request
	) {
		userService.createUser(request);
		return ResponseEntity.ok()
			.body(new APIResponse(EntryPayloadCode.SUCCESS_REQUEST));
	}

	@GetMapping("/my")
	public ResponseEntity<UserResponse> getUserInfo() {
		UserResponse response = userService.getUserInfo();
		return ResponseEntity.ok().body(response);
	}

	@PatchMapping("/my/nickname")
	public ResponseEntity<Void> updateUser(
		@RequestBody @Valid final NicknameUpdateRequest request
	) {
		userService.updateNickname(request);
		return ResponseEntity.ok().build();
	}
//	@PatchMapping("/v1/password")
//	public ResponseEntity<Void> updatePassword (
//		@RequestBody @Valid final PasswordUpdateRequest request
//	){
//		userService.updatePassword(request);
//		return ResponseEntity.ok().build();
//	}
}
