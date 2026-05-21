package coffeetime.domain;

import coffeetime.controller.response.ServerAlertResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "serverAlertService", url = "${logging.discord.webhook-url}")
public interface ServerAlertService {

	@PostMapping
	void sendServerAlarm(@RequestBody ServerAlertResponse serverAlertResponse);
}
