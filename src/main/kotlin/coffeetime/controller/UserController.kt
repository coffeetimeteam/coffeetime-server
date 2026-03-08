package coffeetime.controller;

import coffeetime.controller.request.CreateUserRequest
import coffeetime.controller.request.NicknameUpdateRequest
import coffeetime.controller.response.UserResponse
import coffeetime.domain.UserService;
import coffeetime.support.error.EntryPayloadCode
import coffeetime.support.response.ApiStatus
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
class UserController(
	private val userService: UserService
) {

	@PostMapping("/auth/signup")
	fun createUser(
		@RequestBody @Valid request: CreateUserRequest
	): ResponseEntity<ApiStatus> {
		userService.create(request.username, request.password, request.confirmPassword)
		return ResponseEntity.ok().body(ApiStatus.from(EntryPayloadCode.SUCCESS_REQUEST))
	}

	@GetMapping("/my")
	fun getMemberInfo(
		@RequestHeader(HttpHeaders.AUTHORIZATION) token: String
	): ResponseEntity<UserResponse> {
		val user = userService.getCurrentUser()
		return ResponseEntity.ok().body(UserResponse.from(user));
	}

//	@Deprecated
//	@PatchMapping("/my/nickname")
//	fun updateUser(
//		@RequestBody @Valid request: NicknameUpdateRequest
//	): ResponseEntity<Void> {
//		userService.updateNickname(request);
//		return ResponseEntity.ok().build();
//	}
//
//	@Deprecated
//	@PatchMapping("/v1/password")
//	fun updatePassword (
//		@RequestBody @Valid final PasswordUpdateRequest request
//	): ResponseEntity<Void> {
//		userService.updatePassword(request);
//		return ResponseEntity.ok().build();
//	}
}
