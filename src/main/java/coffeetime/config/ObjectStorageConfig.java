package coffeetime.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Configuration;

import java.net.URI;

@Configuration
public class ObjectStorageConfig {

    @Value("${oci.object-storage.namespace}")
    private String namespace;

	@Value("${oci.object-storage.region}")
	private String region;

    @Value("${oci.object-storage.access-key}")
    private String accessKey;

    @Value("${oci.object-storage.secret-key}")
    private String secretKey;

	@Bean
	public S3Client s3Client() {
		return S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(accessKey, secretKey)
                ))
                .endpointOverride(URI.create("https://" + namespace + ".compat.objectstorage." + region + ".oraclecloud.com"))
                .serviceConfiguration(S3Configuration.builder()
                                                     .pathStyleAccessEnabled(true)
                                                     .chunkedEncodingEnabled(false)
                                                     .build())
                .build();
	}
}