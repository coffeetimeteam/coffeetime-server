package coffeetime.controller;

import coffeetime.domain.ServerAlertService;
import coffeetime.support.auth.ServerAlertGenerator;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ServerAlertController {

	private final ServerAlertService serverAlertService;
	private final ServerAlertGenerator serverAlertGenerator;

	public void sendServerAlertMessage(HttpServletRequest request, Exception e) {
		serverAlertService.sendServerAlarm(serverAlertGenerator.createMessage(request, e));
	}
}
