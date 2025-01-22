package coffeetime.controller;

import coffeetime.domain.User;
import coffeetime.dto.GlobalResponse;
import coffeetime.dto.UserCreateRequest;
import coffeetime.dto.UserResponse;
import coffeetime.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
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
	public ResponseEntity<GlobalResponse> createUser(
		@RequestBody @Valid final UserCreateRequest request
	) {
		final GlobalResponse response = userService.createUser(request);
		return ResponseEntity.ok().body(response);
	}

	@GetMapping("/my")
	@PreAuthorize("isAuthenticated()")
	@Operation(
		security = {@SecurityRequirement(name = "bearerToken")},
		parameters = {
			@Parameter(
				name = HttpHeaders.AUTHORIZATION,
				required = true,
				in = ParameterIn.HEADER,
				schema = @Schema(type = "string", format = "bearer")
			)
		}
	)
	public ResponseEntity<UserResponse> getUserInfo(
	) {
		final User user = userService.getCurrentUser();
		final UserResponse response = new UserResponse(user.getId(), user.getUsername(),
			user.getNickname(), user.getRole());
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
