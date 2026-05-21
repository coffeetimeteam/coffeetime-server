package coffeetime.config;

import java.net.URI;
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Configuration;

@Configuration
@EnableConfigurationProperties(ObjectStorageConfig.ObjectStorageProperties::class)
class ObjectStorageConfig(
	private val properties: ObjectStorageProperties
) {

	@ConfigurationProperties(prefix = "oci.object-storage")
	data class ObjectStorageProperties(
		val namespace: String,
		val region: String,
		val accessKey: String,
		val secretKey: String,
	)

	@Bean
	fun s3Client(): S3Client {
		return S3Client.builder()
			.region(Region.of(properties.region))
			.credentialsProvider(
				StaticCredentialsProvider.create(
					AwsBasicCredentials.create(
						properties.accessKey,
						properties.secretKey
					)
				)
			)
			.endpointOverride(
				URI.create(
					"https://${properties.namespace}.compat.objectstorage.${properties.region}.oraclecloud.com"
					)
			)
			.serviceConfiguration(
				S3Configuration.builder()
					.pathStyleAccessEnabled(true)
					.chunkedEncodingEnabled(false)
					.build())
			.build();
	}
}