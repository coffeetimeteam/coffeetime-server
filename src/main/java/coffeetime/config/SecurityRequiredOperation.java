package coffeetime.config;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.http.HttpHeaders;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Operation(security = {@SecurityRequirement(name = "bearerToken")},
	parameters = {
		@Parameter(name = HttpHeaders.AUTHORIZATION,
			required = true,
			in = ParameterIn.HEADER,
			schema = @Schema(type = "string", format = "bearer"))
	})
public @interface SecurityRequiredOperation {

	String summary() default "";

	String description() default "";
}