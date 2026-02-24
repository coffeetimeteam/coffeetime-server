package coffeetime.config;

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.enums.ParameterIn
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import org.springframework.core.annotation.AliasFor
import org.springframework.http.HttpHeaders

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@Operation(
	security = [SecurityRequirement(name = "bearerToken")],
	parameters = [
		Parameter(
			name = HttpHeaders.AUTHORIZATION,
			required = true,
			`in` = ParameterIn.HEADER,
			schema = Schema(type = "string", format = "bearer")
		)
	]
)
annotation class SecurityRequiredOperation (
	@get:AliasFor(annotation = Operation::class, attribute = "summary")
    val summary: String = "",
	@get:AliasFor(annotation = Operation::class, attribute = "description")
	val description: String = ""
)