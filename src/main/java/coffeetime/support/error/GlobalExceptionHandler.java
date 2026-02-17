package coffeetime.support.error;

import coffeetime.controller.ServerAlertController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(
		GlobalExceptionHandler.class);
	private final ServerAlertController serverAlertController;

	@ResponseBody
	@ExceptionHandler(CoffeeTimeException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ErrorPayloadProvider handleCoffeeTimeException(HttpServletRequest request,
		CoffeeTimeException e) {
		serverAlertController.sendServerAlertMessage(request, e);

		LOGGER.error(e.getMessage(), e);
		List<String> errors = new ArrayList<>();
		errors.add(e.getMessage());
		return new ErrorPayloadProvider(
			HttpStatus.BAD_REQUEST.value(),
			errors,
			request.getServletPath()
		);
	}

	@ResponseBody
	@ExceptionHandler(ConstraintViolationException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ErrorPayloadProvider handleConstraintViolationException(HttpServletRequest request,
		Exception e) {
		serverAlertController.sendServerAlertMessage(request, e);

		LOGGER.error("ConstraintViolationException: {}", e.getMessage(), e);
		List<String> errors = new ArrayList<>();
		errors.add(e.getMessage());
		return new ErrorPayloadProvider(
			HttpStatus.BAD_REQUEST.value(),
			errors,
			request.getServletPath()
		);
	}

	@ResponseBody
	@ExceptionHandler(JwtValidationException.class)
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	public ErrorPayloadProvider handleJwtValidationException(HttpServletRequest request,
		Exception e) {
		serverAlertController.sendServerAlertMessage(request, e);

		LOGGER.error("JwtValidationException: {}", e.getMessage(), e);
		List<String> errors = new ArrayList<>();
		errors.add(e.getMessage());
		return new ErrorPayloadProvider(
			HttpStatus.UNAUTHORIZED.value(),
			errors,
			request.getServletPath()
		);
	}

	@ResponseBody
	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public ErrorPayloadProvider handleException(HttpServletRequest request,
		Exception e) {
		serverAlertController.sendServerAlertMessage(request, e);

		LOGGER.error(e.getMessage(), e);
		List<String> errors = new ArrayList<>();
		errors.add(e.getMessage());
		return new ErrorPayloadProvider(
			HttpStatus.INTERNAL_SERVER_ERROR.value(),
			errors,
			request.getServletPath()
		);
	}

	protected ResponseEntity<Object> handleMethodArgumentNotValid(
		MethodArgumentNotValidException e,
		HttpHeaders headers,
		HttpStatusCode status,
		WebRequest request
	) {
		serverAlertController.sendServerAlertMessage((HttpServletRequest) request, e);

		LOGGER.error(e.getMessage(), e);
		List<String> errors = new ArrayList<>();
		errors.add(e.getBindingResult().getFieldErrors().toString());
		return new ResponseEntity<>(errors, headers, status);
	}
}
