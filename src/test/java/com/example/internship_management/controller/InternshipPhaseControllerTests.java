package com.example.internship_management.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.internship_management.dto.response.InternshipPhaseResponse;
import com.example.internship_management.exception.GlobalExceptionHandler;
import com.example.internship_management.exception.InvalidInputException;
import com.example.internship_management.exception.ResourceNotFoundException;
import com.example.internship_management.service.InternshipPhaseService;
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
class InternshipPhaseControllerTests {

	private MockMvc mockMvc;

	@Mock
	private InternshipPhaseService internshipPhaseService;

	@BeforeEach
	void setUp() {
		mockMvc = MockMvcBuilders.standaloneSetup(new InternshipPhaseController(internshipPhaseService))
				.setControllerAdvice(new GlobalExceptionHandler())
				.build();
	}

	@Test
	void getPhases_shouldReturnPhaseList() throws Exception {
		when(internshipPhaseService.getAllPhases()).thenReturn(List.of(phaseResponse(1)));

		mockMvc.perform(get("/api/internship_phases"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.success").value(true))
				.andExpect(jsonPath("$.status_code").value(200))
				.andExpect(jsonPath("$.data[0].phaseId").value(1))
				.andExpect(jsonPath("$.timestamp").exists());
	}

	@Test
	void getPhaseById_shouldReturnPhaseDetail() throws Exception {
		when(internshipPhaseService.getPhaseById(1)).thenReturn(phaseResponse(1));

		mockMvc.perform(get("/api/internship_phases/1"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.success").value(true))
				.andExpect(jsonPath("$.data.phaseId").value(1));
	}

	@Test
	void getPhaseById_shouldReturnNotFound_whenPhaseMissing() throws Exception {
		when(internshipPhaseService.getPhaseById(99))
				.thenThrow(new ResourceNotFoundException("Không tìm thấy giai đoạn thực tập"));

		mockMvc.perform(get("/api/internship_phases/99"))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.success").value(false))
				.andExpect(jsonPath("$.error_code").value("RESOURCE_NOT_FOUND"));
	}

	@Test
	void createPhase_shouldReturnCreatedPhase() throws Exception {
		when(internshipPhaseService.createPhase(any())).thenReturn(phaseResponse(1));

		mockMvc.perform(post("/api/internship_phases")
						.contentType("application/json")
						.content("{\"phaseName\":\"Internship Phase 1\",\"startDate\":\"2026-08-01\","
								+ "\"endDate\":\"2026-12-01\",\"description\":\"Phase\"}"))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.success").value(true))
				.andExpect(jsonPath("$.status_code").value(201))
				.andExpect(jsonPath("$.data.phaseId").value(1));
	}

	@Test
	void createPhase_shouldReturnInvalidInput_whenDateRangeInvalid() throws Exception {
		when(internshipPhaseService.createPhase(any()))
				.thenThrow(new InvalidInputException("Ngày bắt đầu không được sau ngày kết thúc"));

		mockMvc.perform(post("/api/internship_phases")
						.contentType("application/json")
						.content("{\"phaseName\":\"Invalid Phase\",\"startDate\":\"2026-12-01\","
								+ "\"endDate\":\"2026-08-01\"}"))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.success").value(false))
				.andExpect(jsonPath("$.error_code").value("INVALID_INPUT_DATA"));
	}

	@Test
	void updatePhase_shouldReturnUpdatedPhase() throws Exception {
		when(internshipPhaseService.updatePhase(eq(1), any())).thenReturn(phaseResponse(1));

		mockMvc.perform(put("/api/internship_phases/1")
						.contentType("application/json")
						.content("{\"phaseName\":\"Internship Phase 1\",\"startDate\":\"2026-08-01\","
								+ "\"endDate\":\"2026-12-01\",\"description\":\"Updated\"}"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.success").value(true))
				.andExpect(jsonPath("$.data.phaseId").value(1));
	}

	@Test
	void deletePhase_shouldReturnSuccessWithNullData() throws Exception {
		mockMvc.perform(delete("/api/internship_phases/1"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.success").value(true))
				.andExpect(jsonPath("$.data").doesNotExist());

		verify(internshipPhaseService).deletePhase(1);
	}

	private InternshipPhaseResponse phaseResponse(Integer id) {
		return new InternshipPhaseResponse(id, "Internship Phase " + id, LocalDate.of(2026, 8, 1),
				LocalDate.of(2026, 12, 1), "Phase", null, null);
	}
}
