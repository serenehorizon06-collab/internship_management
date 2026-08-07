package com.example.internship_management.dto.response;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import com.example.internship_management.entity.Student;
import com.example.internship_management.entity.User;
import com.example.internship_management.entity.UserRole;
import org.junit.jupiter.api.Test;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

class StudentResponseSerializationTests {

	private final ObjectMapper objectMapper = new ObjectMapper();

	@Test
	void studentResponseDoesNotExposePasswordHash() throws Exception {
		Student student = new Student();
		student.setStudentId(1);
		student.setStudentCode("SV001");
		User user = new User();
		user.setUserId(1);
		user.setUsername("student01");
		user.setPasswordHash("$2a$10$hashed");
		user.setFullName("Student One");
		user.setEmail("student@example.com");
		user.setRole(UserRole.STUDENT);
		user.setIsActive(true);

		JsonNode json = objectMapper.readTree(objectMapper.writeValueAsString(
				StudentResponse.from(student, UserSummaryResponse.from(user))));

		assertEquals("SV001", json.get("studentCode").asText());
		assertEquals("student01", json.get("user").get("username").asText());
		assertFalse(json.has("passwordHash"));
		assertFalse(json.get("user").has("passwordHash"));
	}
}
