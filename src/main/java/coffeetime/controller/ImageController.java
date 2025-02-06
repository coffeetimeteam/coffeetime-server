package coffeetime.controller;

import coffeetime.config.SecurityRequiredOperation;
import coffeetime.domain.Member;
import coffeetime.dto.CoffeeImageResponse;
import coffeetime.service.ImageService;
import coffeetime.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/images")
public class ImageController {

	private final ImageService imageService;
	private final MemberService memberService;

	@GetMapping("/coffee")
	@SecurityRequiredOperation
	public ResponseEntity<CoffeeImageResponse> getImages() {
		final Member member = memberService.getCurrentUser();
		final CoffeeImageResponse response = imageService.findCoffeeImages(member);
		return ResponseEntity.ok().body(response);
	}
}