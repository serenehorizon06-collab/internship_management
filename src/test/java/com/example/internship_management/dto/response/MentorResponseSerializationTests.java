package com.example.internship_management.dto.response;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import com.example.internship_management.entity.Mentor;
import com.example.internship_management.entity.User;
import com.example.internship_management.entity.UserRole;
import org.junit.jupiter.api.Test;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

class MentorResponseSerializationTests {

	private final ObjectMapper objectMapper = new ObjectMapper();

	@Test
	void mentorResponseDoesNotExposePasswordHash() throws Exception {
		Mentor mentor = new Mentor();
		mentor.setMentorId(1);
		mentor.setDepartment("Computer Science");
		User user = new User();
		user.setUserId(1);
		user.setUsername("mentor01");
		user.setPasswordHash("$2a$10$hashed");
		user.setFullName("Mentor One");
		user.setEmail("mentor@example.com");
		user.setRole(UserRole.MENTOR);
		user.setIsActive(true);

		JsonNode json = objectMapper.readTree(objectMapper.writeValueAsString(
				MentorResponse.from(mentor, UserSummaryResponse.from(user))));

		assertEquals("Computer Science", json.get("department").asText());
		assertEquals("mentor01", json.get("user").get("username").asText());
		assertFalse(json.has("passwordHash"));
		assertFalse(json.get("user").has("passwordHash"));
	}
}
