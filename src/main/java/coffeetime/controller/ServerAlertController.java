package coffeetime.controller;

import coffeetime.infrastructure.ServerAlertGenerator;
import coffeetime.service.ServerAlertService;
import feign.FeignException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ServerAlertController {

	private final ServerAlertService serverAlertService;
	private final ServerAlertGenerator serverAlertGenerator;

	public void sendServerAlertMessage(HttpServletRequest request, Exception e) {
		try {
			serverAlertService.sendServerAlarm(serverAlertGenerator.createMessage(request, e));
		} catch (FeignException ex) {
			log.error("Failed to send server alert: {}", e.getMessage(), ex);
		}
	}
}
