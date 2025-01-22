package coffeetime.controller;

import coffeetime.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/images")
public class ImageController {

	private final ImageService imageService;

	@GetMapping(value = "/{objectKey}", produces = MediaType.IMAGE_JPEG_VALUE)
	public ResponseEntity<byte[]> getImage(@PathVariable String objectKey) {
		byte[] imageBytes = imageService.getImageBytes(objectKey);
		return ResponseEntity.ok()
			.contentType(MediaType.IMAGE_JPEG)
			.body(imageBytes);
	}

	@GetMapping("/url/{objectKey}")
	public ResponseEntity<String> getImageUrl(@PathVariable String objectKey) {
		String presignedUrl = imageService.getPresignedUrl(objectKey);
		return ResponseEntity.ok(presignedUrl);
	}
} 