package coffeetime.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ServerAlertEmbedResponse {
	@JsonProperty("title")
	private String title;

	@JsonProperty("description")
	private String description;
}
