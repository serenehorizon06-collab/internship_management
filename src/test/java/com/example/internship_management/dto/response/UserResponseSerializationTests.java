package com.example.internship_management.dto.response;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import com.example.internship_management.entity.User;
import com.example.internship_management.entity.UserRole;
import org.junit.jupiter.api.Test;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

class UserResponseSerializationTests {

	private final ObjectMapper objectMapper = new ObjectMapper();

	@Test
	void userResponseDoesNotExposePasswordHash() throws Exception {
		User user = new User();
		user.setUserId(1);
		user.setUsername("admin");
		user.setPasswordHash("$2a$10$hashed");
		user.setFullName("System Admin");
		user.setEmail("admin@example.com");
		user.setRole(UserRole.ADMIN);
		user.setIsActive(true);

		JsonNode json = objectMapper.readTree(objectMapper.writeValueAsString(UserResponse.from(user)));

		assertEquals("admin", json.get("username").asText());
		assertEquals(true, json.get("isActive").asBoolean());
		assertFalse(json.has("passwordHash"));
		assertFalse(json.has("active"));
	}
}
