package coffeetime.controller;

import coffeetime.config.SecurityRequiredOperation;
import coffeetime.domain.User;
import coffeetime.dto.CoffeeCreateRequest;
import coffeetime.dto.CoffeeFormResponse;
import coffeetime.dto.CoffeeResponse;
import coffeetime.dto.GlobalResponse;
import coffeetime.exception.EntryPayloadCode;
import coffeetime.service.CoffeeService;
import coffeetime.service.UserService;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@RequestMapping("/api/v1/coffee")
public class CoffeeController {

	private final UserService userService;
	private final CoffeeService coffeeService;

	@GetMapping("/calendar")
	@SecurityRequiredOperation
	public ResponseEntity<List<CoffeeResponse>> getCoffeesByMonth(
		@RequestParam(required = false) final Integer year, final Integer month
	) {
		final User currentUser = userService.getCurrentUser();
		final List<CoffeeResponse> response = coffeeService.findCoffeesByMonth(currentUser, year,
			month);
		return ResponseEntity.ok().body(response);
	}

	@GetMapping
	@SecurityRequiredOperation
	public ResponseEntity<List<CoffeeResponse>> getCoffee(
		@RequestParam(required = false) final LocalDate date
	) {
		final User currentUser = userService.getCurrentUser();
		final List<CoffeeResponse> response = coffeeService.findCoffeesByDate(currentUser, date);
		return ResponseEntity.ok().body(response);
	}

	@GetMapping("/form")
	@SecurityRequiredOperation
	public ResponseEntity<CoffeeFormResponse> createCoffee() {
		final CoffeeFormResponse response = coffeeService.getForm();
		return ResponseEntity.ok().body(response);
	}

	@PostMapping("/create")
	@SecurityRequiredOperation
	public ResponseEntity<GlobalResponse> createCoffeeTime(
		@Valid @ModelAttribute CoffeeCreateRequest request,
		@RequestParam(value = "images", required = false) List<MultipartFile> images) {
		final User currentUser = userService.getCurrentUser();
		coffeeService.createCoffee(currentUser, request, images);
		return ResponseEntity.ok().body(new GlobalResponse(EntryPayloadCode.SUCCESS_REQUEST));
	}
}
