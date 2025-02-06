package coffeetime;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest(classes = CoffeetimeServerApplication.class)
@Testcontainers
class CoffeetimeServerApplicationTests {


	@Container
	static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:latest")
		.withDatabaseName("test")
		.withUsername("user")
		.withPassword("password");

	@DynamicPropertySource
	static void registerPgProperties(DynamicPropertyRegistry registry) {
		registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
		registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
		registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
		registry.add("spring.jpa.hibernate.ddl-auto", () -> "create");
		registry.add("spring.jpa.show-sql", () -> "true");
		registry.add("spring.jpa.generate-ddl", () -> "true");
		registry.add("spring.jpa.properties.hibernate.dialect",
			() -> "org.hibernate.dialect.PostgreSQLDialect");
	}

	@Test
	void contextLoads() {
	}
}
