package coffeetime.service;

import coffeetime.domain.Image;
import coffeetime.domain.ImageFile;
import coffeetime.domain.Member;
import coffeetime.dto.CoffeeImageResponse;
import coffeetime.exception.CoffeeTimeException;
import coffeetime.exception.EntryPayloadCode;
import coffeetime.repository.ImageRepository;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
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

@Service
@RequiredArgsConstructor
@Transactional
public class ImageService {

	@Value("${spring.cloud.aws.s3.bucket}")
	private String bucket;

	private final S3Client s3Client;
	private final ImageRepository imageRepository;
	private final MemberService memberService;

	private final String IMAGE_PREFIX = "coffee/";

	public List<String> uploadImages(List<MultipartFile> multipartFiles) {
		List<CompletableFuture<String>> futures = multipartFiles.stream()
			.map(this::uploadImageToBucketAsync).toList();
		return futures.stream()
			.map(CompletableFuture::join)
			.collect(Collectors.toList());
	}

	private CompletableFuture<String> uploadImageToBucketAsync(final MultipartFile file) {
		return CompletableFuture.supplyAsync(() -> uploadImageToBucket(file));
	}

	private String uploadImageToBucket(final MultipartFile file) {
		final ImageFile imageFile = new ImageFile(file);
		final String objectKey = imageFile.getFilename();
		final String contentType = getFileContentType(file);
		try {
			final PutObjectRequest putObjectRequest = PutObjectRequest.builder()
				.bucket(bucket)
				.key(IMAGE_PREFIX + objectKey)
				.contentType(contentType)
				.build();
			s3Client.putObject(putObjectRequest,
				RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
			return getS3ObjectUrl(IMAGE_PREFIX + objectKey);
		} catch (IOException e) {
			throw new CoffeeTimeException(EntryPayloadCode.FAIL_IMAGE_UPLOAD);
		}
	}

	public CoffeeImageResponse findCoffeeImages(final String token) {
		final Member member = memberService.getCurrentMember(token);
		final List<Image> images = imageRepository.findByCoffee_Member(member);
		final List<String> imageUrls = images.stream().map(Image::getUrl).toList();
		if (member.getId() == null) {
			throw new CoffeeTimeException(EntryPayloadCode.NOT_FOUND_IMAGE);
		}
		return CoffeeImageResponse.getCoffeeImages(imageUrls);
	}

	public void deleteImages(List<String> imageUrls) {
		if (imageUrls.isEmpty()) {
			throw new CoffeeTimeException(EntryPayloadCode.NOT_FOUND_IMAG_DELETE);
		}

		for (String imageUrl : imageUrls) {
			final String objectKey = extractObjectKey(imageUrl);
			final DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
				.bucket(bucket)
				.key(IMAGE_PREFIX + objectKey)
				.build();
			try {
				s3Client.deleteObject(deleteObjectRequest);
			} catch (Exception e) {
				throw new CoffeeTimeException(EntryPayloadCode.FAIL_IMAGE_DELETE);
			}
		}
	}

	private String getFileContentType(final MultipartFile file) {
		final String originalFilename = file.getOriginalFilename();
		final String contentType = file.getContentType();
		if (originalFilename != null && originalFilename.toLowerCase().endsWith(".heic")) {
			return "image/heic";
		}
		return contentType != null ? contentType : "application/octet-stream";
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