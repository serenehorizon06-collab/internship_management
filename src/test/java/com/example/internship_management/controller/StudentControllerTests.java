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

import com.example.internship_management.dto.response.StudentResponse;
import com.example.internship_management.dto.response.UserSummaryResponse;
import com.example.internship_management.entity.UserRole;
import com.example.internship_management.exception.GlobalExceptionHandler;
import com.example.internship_management.exception.ResourceNotFoundException;
import com.example.internship_management.service.StudentService;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
class StudentControllerTests {

	private MockMvc mockMvc;

	@Mock
	private StudentService studentService;

	@BeforeEach
	void setUp() {
		mockMvc = MockMvcBuilders.standaloneSetup(new StudentController(studentService))
				.setControllerAdvice(new GlobalExceptionHandler())
				.build();
	}

	@Test
	void getStudents_shouldReturnStudents() throws Exception {
		when(studentService.getStudentsForCurrentUser()).thenReturn(List.of(studentResponse(2)));

		mockMvc.perform(get("/api/students"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.success").value(true))
				.andExpect(jsonPath("$.status_code").value(200))
				.andExpect(jsonPath("$.data[0].studentCode").value("SV002"))
				.andExpect(content().string(not(containsString("passwordHash"))));
	}

	@Test
	void getStudentById_shouldReturnStudentDetail() throws Exception {
		when(studentService.getStudentById(2)).thenReturn(studentResponse(2));

		mockMvc.perform(get("/api/students/2"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.success").value(true))
				.andExpect(jsonPath("$.data.studentId").value(2))
				.andExpect(jsonPath("$.timestamp").exists());
	}

	@Test
	void getStudentById_shouldReturnNotFound_whenStudentMissing() throws Exception {
		when(studentService.getStudentById(99)).thenThrow(new ResourceNotFoundException("Không tìm thấy sinh viên"));

		mockMvc.perform(get("/api/students/99"))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.success").value(false))
				.andExpect(jsonPath("$.error_code").value("RESOURCE_NOT_FOUND"));
	}

	@Test
	void createStudent_shouldReturnCreatedStudent() throws Exception {
		when(studentService.createStudent(any())).thenReturn(studentResponse(2));

		mockMvc.perform(post("/api/students")
						.contentType("application/json")
						.content("{\"studentId\":2,\"studentCode\":\"SV002\",\"major\":\"IT\","
								+ "\"className\":\"IT01\",\"dateOfBirth\":\"2004-01-01\",\"address\":\"Hanoi\"}"))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.success").value(true))
				.andExpect(jsonPath("$.status_code").value(201))
				.andExpect(jsonPath("$.data.studentCode").value("SV002"));
	}

	@Test
	void updateStudent_shouldReturnUpdatedStudent() throws Exception {
		when(studentService.updateStudent(eq(2), any())).thenReturn(studentResponse(2));

		mockMvc.perform(put("/api/students/2")
						.contentType("application/json")
						.content("{\"studentCode\":\"SV002\",\"major\":\"IT\","
								+ "\"className\":\"IT02\",\"dateOfBirth\":\"2004-01-01\",\"address\":\"Hanoi\"}"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.success").value(true))
				.andExpect(jsonPath("$.data.studentId").value(2));
	}

	private StudentResponse studentResponse(Integer id) {
		UserSummaryResponse user = new UserSummaryResponse(id, "student" + id, "Student " + id,
				"student" + id + "@example.com", null, UserRole.STUDENT, true);
		return new StudentResponse(id, "SV00" + id, "IT", "IT01", LocalDate.of(2004, 1, 1),
				"Hanoi", null, null, user);
	}
}
