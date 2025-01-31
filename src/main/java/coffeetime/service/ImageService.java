package coffeetime.service;

import coffeetime.domain.Image;
import coffeetime.domain.ImageFile;
import coffeetime.domain.User;
import coffeetime.dto.CoffeeImageResponse;
import coffeetime.exception.CoffeeTimeException;
import coffeetime.exception.EntryPayloadCode;
import coffeetime.repository.ImageRepository;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

@Service
@RequiredArgsConstructor
@Transactional
public class ImageService {

	@Value("${spring.cloud.aws.s3.bucket}")
	private String bucket;

	private final S3Presigner s3Presigner;
	private final S3Client s3Client;
	private final ImageRepository imageRepository;

	private final String IMAGE_PREFIX = "coffee/";

	public List<String> uploadImages(List<MultipartFile> multipartFiles) {
		return multipartFiles.stream()
			.map(this::uploadImage)
			.collect(Collectors.toList());
	}

	private String uploadImage(final MultipartFile file) {
		final ImageFile imageFile = new ImageFile(file);
		final String objectKey = imageFile.getFilename();
		try {
			final PutObjectRequest putObjectRequest = PutObjectRequest.builder()
				.bucket(bucket)
				.key(IMAGE_PREFIX + objectKey)
				.contentType(file.getContentType())
				.build();
			s3Client.putObject(putObjectRequest,
				RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
			return getS3ObjectUrl(IMAGE_PREFIX + objectKey);
		} catch (IOException e) {
			throw new CoffeeTimeException(EntryPayloadCode.FAIL_IMAGE_UPLOAD);
		}
	}

	public CoffeeImageResponse findCoffeeImages(final User user) {
		final List<Image> images = imageRepository.findByCoffee_User(user);
		final List<String> imageUrls = images.stream().map(Image::getUrl).toList();
		if (user.getId() == null) {
			throw new CoffeeTimeException(EntryPayloadCode.NOT_FOUND_IMAGE);
		}
		return CoffeeImageResponse.getCoffeeImages(imageUrls);
	}

	public void deleteImage(String url) {
		String objectKey = extractObjectKey(url);
		final DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
			.bucket(bucket)
			.key(IMAGE_PREFIX + objectKey)
			.build();
		s3Client.deleteObject(deleteObjectRequest);
	}

	private String getS3ObjectUrl(final String objectKey) {
		return String.format("https://%s/%s",
			bucket,
			objectKey);
	}

	private String extractObjectKey(String url) {
		return url.substring(url.lastIndexOf("/") + 1);
	}
}