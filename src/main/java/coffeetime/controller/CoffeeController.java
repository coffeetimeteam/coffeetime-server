package coffeetime.controller;

import coffeetime.config.SecurityRequiredOperation;
import coffeetime.dto.CoffeeCreateRequest;
import coffeetime.dto.CoffeeFormResponse;
import coffeetime.dto.CoffeeResponse;
import coffeetime.dto.CoffeeUpdateRequest;
import coffeetime.dto.GlobalResponse;
import coffeetime.exception.EntryPayloadCode;
import coffeetime.service.CoffeeService;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@RequestMapping("/api/v1/coffee")
public class CoffeeController {

	private final CoffeeService coffeeService;

	@PostMapping("/create")
	@SecurityRequiredOperation
	public ResponseEntity<GlobalResponse> createCoffee(
		@Valid @ModelAttribute CoffeeCreateRequest request
	) {
		final GlobalResponse response = coffeeService.createCoffee(request);
		return ResponseEntity.ok().body(response);
	}

	@GetMapping("/form")
	@SecurityRequiredOperation
	public ResponseEntity<CoffeeFormResponse> createCoffee() {
		final CoffeeFormResponse response = coffeeService.getForm();
		return ResponseEntity.ok().body(response);
	}

	@GetMapping
	@SecurityRequiredOperation
	public ResponseEntity<List<CoffeeResponse>> getCoffeesByDate(
		@RequestParam(required = false) final LocalDate date
	) {
		final List<CoffeeResponse> response = coffeeService.findCoffeesByDate(date);
		return ResponseEntity.ok().body(response);
	}


	@GetMapping("/calendar")
	@SecurityRequiredOperation
	public ResponseEntity<List<Map<String, Object>>> getCoffeesByMonth(
		@RequestParam(required = false) final Integer year,
		@RequestParam(required = false) final Integer month
	) {
		final List<Map<String, Object>> response = coffeeService.findCoffeesByMonth(
			year, month);
		return ResponseEntity.ok().body(response);
	}


	@PutMapping("/{coffeeId}")
	@SecurityRequiredOperation
	public ResponseEntity<GlobalResponse> updateCoffee(
		@PathVariable final Long coffeeId,
		@Valid @ModelAttribute final CoffeeUpdateRequest request) {
		coffeeService.updateCoffee(coffeeId, request);
		return ResponseEntity.ok().body(new GlobalResponse(EntryPayloadCode.SUCCESS_REQUEST));
	}

	@DeleteMapping("/{coffeeId}")
	@SecurityRequiredOperation
	public ResponseEntity<GlobalResponse> deleteCoffee(
		@PathVariable final Long coffeeId
	) {
		coffeeService.deleteCoffee(coffeeId);
		return ResponseEntity.ok().body(new GlobalResponse(EntryPayloadCode.SUCCESS_REQUEST));
	}
}
