package coffeetime.controller;

import coffeetime.config.SecurityRequiredOperation;
import coffeetime.dto.CoffeeImageResponse;
import coffeetime.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/images")
public class ImageController {

	private final ImageService imageService;

	@GetMapping("/coffee")
	@SecurityRequiredOperation
	public ResponseEntity<CoffeeImageResponse> getImages(
		@RequestHeader(HttpHeaders.AUTHORIZATION) String token
	) {
		final CoffeeImageResponse response = imageService.findCoffeeImages(token);
		return ResponseEntity.ok().body(response);
	}
}