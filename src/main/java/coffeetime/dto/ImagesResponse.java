package coffeetime.dto;

import coffeetime.domain.Image;
import java.util.List;

public record ImagesResponse(
	List<Long> imageId
) {

	public static ImagesResponse of(List<Image> imageFiles) {
		return new ImagesResponse(imageFiles.stream().map(Image::getId).toList());
	}
}
