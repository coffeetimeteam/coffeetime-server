package coffeetime.support.auth;

import static lombok.AccessLevel.PROTECTED;

import coffeetime.controller.response.ServerAlertEmbedResponse;
import coffeetime.controller.response.ServerAlertResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(access = PROTECTED)
public class ServerAlertGenerator {

	private final HttpServletRequest httpServletRequest;
	@Value("${spring.profiles.active}")
	private String springActiveProfile;

	public ServerAlertResponse createMessage(HttpServletRequest request, Exception e) {
		String formattedTime = ZonedDateTime.now(ZoneId.of("Asia/Seoul"))
			.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

		String description = String.format(
			"time : ⏰ %s\n clinet IP : 🛜 %s\n request url : 🔗 %s\n spring active profile : 🗃️ %s\n error log : 📃 %s",
			formattedTime,
			getClientIP(httpServletRequest),
			request.getRequestURI(),
			springActiveProfile,
			getStackTrace(e).substring(0, 1000)
		);

		return ServerAlertResponse.builder()
			.content("## server: 🚨 Error Warning")
			.embeds(List.of(ServerAlertEmbedResponse.builder()
				.title("☕️ CoffeeTime Server Error Info")
				.description(description)
				.build()))
			.build();
	}

	private String getStackTrace(Exception e) {
		StringWriter stringWriter = new StringWriter();
		e.printStackTrace(new PrintWriter(stringWriter));
		return stringWriter.toString();
	}

	private String getClientIP(HttpServletRequest request) {
		return request.getRemoteAddr();
	}
}
