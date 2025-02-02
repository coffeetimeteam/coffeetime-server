package coffeetime.controller;

import coffeetime.config.SecurityRequiredOperation;
import coffeetime.domain.User;
import coffeetime.dto.CoffeeImageResponse;
import coffeetime.service.ImageService;
import coffeetime.service.UserService;
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
	private final UserService userService;

	@GetMapping("/coffee")
	@SecurityRequiredOperation
	public ResponseEntity<CoffeeImageResponse> getImages() {
		final User user = userService.getCurrentUser();
		final CoffeeImageResponse response = imageService.findCoffeeImages(user);
		return ResponseEntity.ok().body(response);
	}
}