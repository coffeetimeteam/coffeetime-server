package coffeetime.domain;

import coffeetime.support.error.CoffeeTimeException;
import coffeetime.support.error.EntryPayloadCode;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
public class ImageFile {

	private final MultipartFile multipartFile;
	private final String filename;

	public ImageFile(MultipartFile multipartFile) {
		validateImage(multipartFile);
		this.multipartFile = multipartFile;
		this.filename = generateFilename(multipartFile);
	}

	private void validateImage(MultipartFile multipartFile) {
		if (multipartFile.isEmpty()) {
			throw new CoffeeTimeException(EntryPayloadCode.NOT_FOUND_IMAGE);
		}
	}

	private String generateFilename(final MultipartFile multipartFile) {
		final String originalFilename = multipartFile.getOriginalFilename();
		final String extension = (originalFilename != null && originalFilename.contains("."))
			? originalFilename.substring(originalFilename.lastIndexOf("."))
			: "";
		final String uuid = UUID.randomUUID().toString();
		final String uuidAndDate = uuid + LocalDateTime.now();
		return uuid + uuidAndDate + extension;
	}
}
