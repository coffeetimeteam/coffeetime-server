package coffeetime.infrastructure;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import coffeetime.dto.UserCreateRequest;
import coffeetime.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(properties = "spring.profiles.active=test")
@AutoConfigureMockMvc
@Transactional
@Rollback(value = true)
public class SecurityTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private UserService userService;

	private static final String GET_ACCESS_TOKEN_ENDPOINT = "/api/v1/auth/login";
	private static final String GET_MY_ENDPOINT = "/api/v1/my";
	private static final String REFRESH_TOKEN_ENDPOINT = "/api/v1/auth/token";

	// given
	final String username = "security@email.com";
	final String password = "password";

	@BeforeEach
	public void setup() {
		UserCreateRequest request = new UserCreateRequest(
			username,
			password,
			password
		);
		userService.createUser(request);
	}

	@Test
	public void getBaseURIShouldReturn401() throws Exception {
		mockMvc.perform(get("/"))
			.andDo(print())
			.andExpect(status().isUnauthorized());
	}

//	@Test
//	public void testGetAccessTokenBadRequest() throws Exception {
//		// given
//		final String wrongPassword = "";
//
//		// when
//		LoginRequest loginRequest = new LoginRequest(username, wrongPassword);
//		String requestBody = objectMapper.writeValueAsString(loginRequest);
//
//		// then
//		mockMvc.perform(
//				post(GET_ACCESS_TOKEN_ENDPOINT)
//					.contentType("application/json")
//					.content(requestBody))
//			.andDo(print())
//			.andExpect(status().isBadRequest());
//	}
//
//	@Test
//	public void testGetAccessTokenFail() throws Exception {
//		final String wrongPassword = "wrongPassword";
//		LoginRequest request = new LoginRequest(username, wrongPassword);
//
//		String requestBody = objectMapper.writeValueAsString(request);
//
//		mockMvc.perform(
//				post(GET_ACCESS_TOKEN_ENDPOINT)
//					.contentType("application/json")
//					.content(requestBody))
//			.andDo(print())
//			.andExpect(status().isUnauthorized());
//
//	}
//
//	@Test
//	public void testGetAccessTokenSuccess() throws Exception {
//		LoginRequest request = new LoginRequest(username, password);
//
//		String requestBody = objectMapper.writeValueAsString(request);
//
//		mockMvc.perform(
//				post(GET_ACCESS_TOKEN_ENDPOINT)
//					.contentType("application/json")
//					.content(requestBody))
//			.andDo(print())
//			.andExpect(status().isOk());
////			.andExpect(jsonPath("$.accessToken").isNotEmpty())
////			.andExpect(jsonPath("$.refreshToken").isNotEmpty());
//	}
//
//	@Test
//	public void testGetListFail() throws Exception {
//		mockMvc.perform(get(GET_MY_ENDPOINT).header("Authorization",
//				"Bearer something invalid"))
//			.andDo(print())
//			.andExpect(status().isUnauthorized())
//			.andExpect(jsonPath("$.errors").isNotEmpty());
//	}
//
//	@Test
//	public void testListSuccess() throws Exception {
//		LoginRequest request = new LoginRequest(username, password);
//		String requestBody = objectMapper.writeValueAsString(request);
//
//		MvcResult mvcResult = mockMvc.perform(
//				post(GET_ACCESS_TOKEN_ENDPOINT)
//					.contentType("application/json")
//					.content(requestBody))
//			.andDo(print())
//			.andExpect(status().isOk())
//			.andReturn();
//
//		String responseBody = mvcResult.getResponse().getContentAsString();
//		TokensResponse response = objectMapper.readValue(responseBody, TokensResponse.class);
//		String bearerToken = "Bearer " + response.accessToken();
//
//		mockMvc.perform(get(GET_MY_ENDPOINT).header("Authorization", bearerToken))
//			.andDo(print())
//			.andExpect(status().isOk())
//			.andExpect(jsonPath("$.username").isString())
//			.andExpect(jsonPath("$.nickname").isString());
//	}
//
//	@Test
//	public void testRefreshTokenBadRequest() throws Exception {
//		RefreshTokenRequest request = new RefreshTokenRequest(
//			"saiojaiwojifa89we8f9aewfsaiojaiwojifa89we8f9aewfasaiojaiwojifa89we8f9aewfsaiojaiwojifa89we8f9aewfa");
//		String requestBody = objectMapper.writeValueAsString(request);
//		mockMvc.perform(post(REFRESH_TOKEN_ENDPOINT)
//				.contentType("application/json")
//				.content(requestBody))
//			.andDo(print())
//			.andExpect(status().isBadRequest());
//	}
//
//	@Test
//	public void testRefreshTokenFail() throws Exception {
//		RefreshTokenRequest request = new RefreshTokenRequest(
//			"saiojaiwojifa89we8f9aewfsaiojaiwojifa89we8f9aewfasaiojaiwojifa89we8f9aewfsaiojaiwojifa89we8f9aewfa");
//		String requestBody = objectMapper.writeValueAsString(request);
//		mockMvc.perform(post(REFRESH_TOKEN_ENDPOINT)
//				.contentType("application/json")
//				.content(requestBody))
//			.andDo(print())
//			.andExpect(status().isBadRequest());
//	}
//
//	@Test
//	public void testRefreshTokenSuccess() throws Exception {
//		RefreshTokenRequest request = new RefreshTokenRequest(
//			"saiojaiwojifa89we8f9aewfsaiojaiwojifa89we8f9aewfasaiojaiwojifa89we8f9aewfsaiojaiwojifa89we8f9aewfa");
//		String requestBody = objectMapper.writeValueAsString(request);
//		mockMvc.perform(post(REFRESH_TOKEN_ENDPOINT)
//				.contentType("application/json")
//				.content(requestBody))
//			.andDo(print())
//			.andExpect(status().isOk());
//	}
}
