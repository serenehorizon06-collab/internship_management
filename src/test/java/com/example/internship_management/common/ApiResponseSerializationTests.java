package com.example.internship_management.common;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.internship_management.exception.ErrorCode;
import com.example.internship_management.repository.UserRepository;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

@SpringBootTest
@ActiveProfiles("test")
class ApiResponseSerializationTests {

	@MockitoBean
	private UserRepository userRepository;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	void apiResponseSerializesWithSrsFields() throws Exception {
		ApiResponse<Map<String, String>> response = ApiResponse.success("OK", Map.of("value", "demo"));

		JsonNode json = objectMapper.readTree(objectMapper.writeValueAsString(response));

		assertTrue(json.get("success").asBoolean());
		assertEquals(200, json.get("status_code").asInt());
		assertEquals("OK", json.get("message").asText());
		assertEquals("demo", json.get("data").get("value").asText());
		assertTrue(json.has("timestamp"));
		assertFalse(json.has("statusCode"));
	}

	@Test
	void errorResponseSerializesWithSrsFields() throws Exception {
		ErrorResponse response = new ErrorResponse(
				400,
				ErrorCode.INVALID_INPUT_DATA,
				"Invalid",
				List.of(new FieldErrorResponse("score", "Score must be positive")));

		JsonNode json = objectMapper.readTree(objectMapper.writeValueAsString(response));

		assertFalse(json.get("success").asBoolean());
		assertEquals(400, json.get("status_code").asInt());
		assertEquals("INVALID_INPUT_DATA", json.get("error_code").asText());
		assertEquals("Invalid", json.get("message").asText());
		assertEquals("score", json.get("errors").get(0).get("field").asText());
		assertEquals("Score must be positive", json.get("errors").get(0).get("message").asText());
		assertTrue(json.has("timestamp"));
		assertFalse(json.has("statusCode"));
		assertFalse(json.has("errorCode"));
	}
}
