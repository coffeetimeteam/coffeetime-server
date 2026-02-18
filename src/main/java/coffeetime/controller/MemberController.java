package coffeetime.controller;

import coffeetime.controller.request.MemberCreateRequest;
import coffeetime.controller.response.GlobalResponse;
import coffeetime.controller.response.MemberResponse;
import coffeetime.domain.MemberService;
import coffeetime.domain.Member;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class MemberController {

	private final MemberService memberService;

	@PostMapping("/auth/signup")
	public ResponseEntity<GlobalResponse> createMember(
		@RequestBody @Valid final MemberCreateRequest request
	) {
		final GlobalResponse response = memberService.createMember(request);
		return ResponseEntity.ok().body(response);
	}

	@GetMapping("/my")
	public ResponseEntity<MemberResponse> getMemberInfo(
		@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
		final Member member = memberService.getCurrentMember(token);
		final MemberResponse response = new MemberResponse(member.getId(), member.getUsername(),
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
