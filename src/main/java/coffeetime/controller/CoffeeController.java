package coffeetime.controller;

import coffeetime.dto.CoffeeFormResponse;
import coffeetime.dto.GlobalResponse;
import coffeetime.exception.EntryPayloadCode;
import coffeetime.service.CoffeeService;
import coffeetime.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@RequestMapping("/api/v1/coffee")
public class CoffeeController {

	private final UserService userService;
	private final CoffeeService coffeeService;

	@GetMapping("/form")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<CoffeeFormResponse> createCoffee() {
		final CoffeeFormResponse response = coffeeService.getForm();
		return ResponseEntity.ok().body(response);
	}

	@PostMapping("/create")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<GlobalResponse> createCoffeeTime() {
		final Long userId = userService.getCurrentUser().getId();
		return ResponseEntity.ok(new GlobalResponse(EntryPayloadCode.SUCCESS_REQUEST));
	}
}
