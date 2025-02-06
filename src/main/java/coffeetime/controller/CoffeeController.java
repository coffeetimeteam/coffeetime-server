package coffeetime.controller;

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
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@RequestMapping("/api/v1/coffee")
public class CoffeeController {

	private final CoffeeService coffeeService;

	@PostMapping("/create")
	public ResponseEntity<GlobalResponse> createCoffee(
		@Valid @ModelAttribute CoffeeCreateRequest request,
		@RequestHeader(HttpHeaders.AUTHORIZATION) String token
	) {
		final GlobalResponse response = coffeeService.createCoffee(request, token);
		return ResponseEntity.ok().body(response);
	}

	@GetMapping("/form")
	public ResponseEntity<CoffeeFormResponse> createCoffee(
		@RequestHeader(HttpHeaders.AUTHORIZATION) String token
	) {
		final CoffeeFormResponse response = coffeeService.getForm(token);
		return ResponseEntity.ok().body(response);
	}

	@GetMapping
	public ResponseEntity<List<CoffeeResponse>> getCoffeesByDate(
		@RequestParam(required = false) final LocalDate date,
		@RequestHeader(HttpHeaders.AUTHORIZATION) String token
	) {
		final List<CoffeeResponse> response = coffeeService.findCoffeesByDate(date, token);
		return ResponseEntity.ok().body(response);
	}


	@GetMapping("/calendar")
	public ResponseEntity<List<Map<String, Object>>> getCoffeesByMonth(
		@RequestParam(required = false) final Integer year,
		@RequestParam(required = false) final Integer month,
		@RequestHeader(HttpHeaders.AUTHORIZATION) String token
	) {
		final List<Map<String, Object>> response = coffeeService.findCoffeesByMonth(
			year, month, token);
		return ResponseEntity.ok().body(response);
	}


	@PutMapping("/{coffeeId}")
	public ResponseEntity<GlobalResponse> updateCoffee(
		@PathVariable final Long coffeeId,
		@Valid @ModelAttribute final CoffeeUpdateRequest request,
		@RequestHeader(HttpHeaders.AUTHORIZATION) String token
	) {
		coffeeService.updateCoffee(coffeeId, request, token);
		return ResponseEntity.ok().body(new GlobalResponse(EntryPayloadCode.SUCCESS_REQUEST));
	}

	@DeleteMapping("/{coffeeId}")
	public ResponseEntity<GlobalResponse> deleteCoffee(
		@PathVariable final Long coffeeId,
		@RequestHeader(HttpHeaders.AUTHORIZATION) String token
	) {
		coffeeService.deleteCoffee(coffeeId, token);
		return ResponseEntity.ok().body(new GlobalResponse(EntryPayloadCode.SUCCESS_REQUEST));
	}
}
