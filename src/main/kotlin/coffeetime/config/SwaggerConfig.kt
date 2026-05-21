package coffeetime.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class SwaggerConfig {

	@Bean
	fun openAPI(): OpenAPI {
		return OpenAPI()
			.components(Components())
			.info(apiInfo());
	}

	private fun apiInfo(): Info {
		return Info()
			.title("CoffeeTime API Document")
			.description("CoffeeTime API 문서입니다.")
			.version("1.0.0");
	}
}

