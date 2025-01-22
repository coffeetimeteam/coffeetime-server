package coffeetime.service;

import coffeetime.domain.ImageFile;
import coffeetime.exception.CoffeeTimeException;
import coffeetime.exception.EntryPayloadCode;
import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.core.sync.RequestBody;

@Service
@RequiredArgsConstructor
public class ImageService {

	@Value("${spring.cloud.aws.s3.bucket}")
	private String bucket;

	@Value("${spring.cloud.aws.region.static}")
	private String region;

	@Value("${cloud.aws.cloudfront.domain}")
	private String cloudfrontDomain;

	private final S3Presigner s3Presigner;
	private final S3Client s3Client;

	private String getS3ObjectUrl(final String objectKey) {
		final GetObjectRequest getObjectRequest = GetObjectRequest.builder()
			.bucket(bucket)
			.key(objectKey)
			.build();
		return s3Presigner.presignGetObject(builder -> builder
			.getObjectRequest(getObjectRequest)
			.signatureDuration(Duration.ofMinutes(20))
			.build())
			.url()
			.toString();
	}

	public List<String> uploadImages(List<MultipartFile> multipartFiles) {
		return multipartFiles.stream()
			.map(this::uploadImage)
			.collect(Collectors.toList());
	}

	private String getObjectKey(final MultipartFile file) {
		final ImageFile imageFile = new ImageFile(file);
		return imageFile.getFilename();
	}

	public String getPresignedUrl(String objectKey) {
		try {
			GetObjectRequest getObjectRequest = GetObjectRequest.builder()
				.bucket(bucket)
				.key(objectKey)
				.build();

			GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
				.signatureDuration(Duration.ofHours(1))
				.getObjectRequest(getObjectRequest)
				.build();

			return s3Presigner.presignGetObject(presignRequest)
				.url()
				.toString();
		} catch (S3Exception e) {
			throw new CoffeeTimeException(EntryPayloadCode.NOT_FOUND_IMAGE);
		}
	}

	private String uploadImage(MultipartFile file) {
		try {
			ImageFile imageFile = new ImageFile(file);
			String objectKey = imageFile.getFilename();
			
			PutObjectRequest putObjectRequest = PutObjectRequest.builder()
				.bucket(bucket)
				.key(objectKey)
				.contentType(file.getContentType())
				.build();

			s3Client.putObject(putObjectRequest,
				RequestBody.fromBytes(file.getBytes()));

			return objectKey;
		} catch (IOException e) {
			throw new CoffeeTimeException(EntryPayloadCode.FAIL_IMAGE_UPLOAD);
		}
	}

	public void deleteImage(String filename) {
		final DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
			.bucket(bucket)
			.key(filename)
			.build();
		s3Client.deleteObject(deleteObjectRequest);
	}

	public byte[] getImageBytes(String objectKey) {
		try {
			GetObjectRequest getObjectRequest = GetObjectRequest.builder()
				.bucket(bucket)
				.key(objectKey)
				.build();
				
			ResponseBytes<GetObjectResponse> objectBytes = s3Client.getObjectAsBytes(getObjectRequest);
			return objectBytes.asByteArray();
		} catch (S3Exception e) {
			throw new CoffeeTimeException(EntryPayloadCode.NOT_FOUND_IMAGE);
		}
	}
}