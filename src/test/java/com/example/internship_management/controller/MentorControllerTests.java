package com.example.internship_management.controller;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.internship_management.dto.response.MentorResponse;
import com.example.internship_management.dto.response.UserSummaryResponse;
import com.example.internship_management.entity.UserRole;
import com.example.internship_management.exception.GlobalExceptionHandler;
import com.example.internship_management.exception.ResourceNotFoundException;
import com.example.internship_management.service.MentorService;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
class MentorControllerTests {

	private MockMvc mockMvc;

	@Mock
	private MentorService mentorService;

	@BeforeEach
	void setUp() {
		mockMvc = MockMvcBuilders.standaloneSetup(new MentorController(mentorService))
				.setControllerAdvice(new GlobalExceptionHandler())
				.build();
	}

	@Test
	void getMentors_shouldReturnMentors() throws Exception {
		when(mentorService.getAllMentors()).thenReturn(List.of(mentorResponse(3)));

		mockMvc.perform(get("/api/mentors"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.success").value(true))
				.andExpect(jsonPath("$.status_code").value(200))
				.andExpect(jsonPath("$.data[0].mentorId").value(3))
				.andExpect(content().string(not(containsString("passwordHash"))));
	}

	@Test
	void getMentorById_shouldReturnMentorDetail() throws Exception {
		when(mentorService.getMentorById(3)).thenReturn(mentorResponse(3));

		mockMvc.perform(get("/api/mentors/3"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.success").value(true))
				.andExpect(jsonPath("$.data.mentorId").value(3));
	}

	@Test
	void getMentorById_shouldReturnNotFound_whenMentorMissing() throws Exception {
		when(mentorService.getMentorById(99))
				.thenThrow(new ResourceNotFoundException("Không tìm thấy giáo viên hướng dẫn"));

		mockMvc.perform(get("/api/mentors/99"))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.success").value(false))
				.andExpect(jsonPath("$.error_code").value("RESOURCE_NOT_FOUND"));
	}

	@Test
	void createMentor_shouldReturnCreatedMentor() throws Exception {
		when(mentorService.createMentor(any())).thenReturn(mentorResponse(3));

		mockMvc.perform(post("/api/mentors")
						.contentType("application/json")
						.content("{\"mentorId\":3,\"department\":\"Software Engineering\",\"academicRank\":\"Lecturer\"}"))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.success").value(true))
				.andExpect(jsonPath("$.status_code").value(201))
				.andExpect(jsonPath("$.data.mentorId").value(3));
	}

	@Test
	void updateMentor_shouldReturnUpdatedMentor() throws Exception {
		when(mentorService.updateMentor(eq(3), any())).thenReturn(mentorResponse(3));

		mockMvc.perform(put("/api/mentors/3")
						.contentType("application/json")
						.content("{\"department\":\"IT\",\"academicRank\":\"Senior Lecturer\"}"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.success").value(true))
				.andExpect(jsonPath("$.data.mentorId").value(3));
	}

	private MentorResponse mentorResponse(Integer id) {
		UserSummaryResponse user = new UserSummaryResponse(id, "mentor" + id, "Mentor " + id,
				"mentor" + id + "@example.com", null, UserRole.MENTOR, true);
		return new MentorResponse(id, "Software Engineering", "Lecturer", null, null, user);
	}
}
