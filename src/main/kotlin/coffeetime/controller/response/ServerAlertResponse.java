package coffeetime.controller.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ServerAlertResponse {

	@JsonProperty("content")
	private String content;

	@JsonProperty("embeds")
	private List<ServerAlertEmbedResponse> embeds;
}