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

import com.example.internship_management.dto.response.EvaluationCriterionResponse;
import com.example.internship_management.exception.GlobalExceptionHandler;
import com.example.internship_management.exception.ResourceNotFoundException;
import com.example.internship_management.service.EvaluationCriterionService;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
class EvaluationCriterionControllerTests {

	private MockMvc mockMvc;

	@Mock
	private EvaluationCriterionService evaluationCriterionService;

	@BeforeEach
	void setUp() {
		mockMvc = MockMvcBuilders.standaloneSetup(new EvaluationCriterionController(evaluationCriterionService))
				.setControllerAdvice(new GlobalExceptionHandler())
				.build();
	}

	@Test
	void getCriteria_shouldReturnCriterionList() throws Exception {
		when(evaluationCriterionService.getAllCriteria()).thenReturn(List.of(criterionResponse(1)));

		mockMvc.perform(get("/api/evaluation_criteria"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.success").value(true))
				.andExpect(jsonPath("$.status_code").value(200))
				.andExpect(jsonPath("$.data[0].criterionId").value(1))
				.andExpect(jsonPath("$.timestamp").exists());
	}

	@Test
	void getCriterionById_shouldReturnCriterionDetail() throws Exception {
		when(evaluationCriterionService.getCriterionById(1)).thenReturn(criterionResponse(1));

		mockMvc.perform(get("/api/evaluation_criteria/1"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.success").value(true))
				.andExpect(jsonPath("$.data.criterionId").value(1));
	}

	@Test
	void getCriterionById_shouldReturnNotFound_whenCriterionMissing() throws Exception {
		when(evaluationCriterionService.getCriterionById(99))
				.thenThrow(new ResourceNotFoundException("Không tìm thấy tiêu chí đánh giá"));

		mockMvc.perform(get("/api/evaluation_criteria/99"))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.success").value(false))
				.andExpect(jsonPath("$.error_code").value("RESOURCE_NOT_FOUND"));
	}

	@Test
	void createCriterion_shouldReturnCreatedCriterion() throws Exception {
		when(evaluationCriterionService.createCriterion(any())).thenReturn(criterionResponse(1));

		mockMvc.perform(post("/api/evaluation_criteria")
						.contentType("application/json")
						.content("{\"criterionName\":\"Work attitude\",\"description\":\"Evaluate responsibility\","
								+ "\"maxScore\":10.00}"))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.success").value(true))
				.andExpect(jsonPath("$.status_code").value(201))
				.andExpect(jsonPath("$.data.criterionId").value(1));
	}

	@Test
	void createCriterion_shouldReturnInvalidInput_whenMaxScoreNotPositive() throws Exception {
		mockMvc.perform(post("/api/evaluation_criteria")
						.contentType("application/json")
						.content("{\"criterionName\":\"Invalid\",\"description\":\"Invalid\",\"maxScore\":0}"))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.success").value(false))
				.andExpect(jsonPath("$.error_code").value("INVALID_INPUT_DATA"));
	}

	@Test
	void updateCriterion_shouldReturnUpdatedCriterion() throws Exception {
		when(evaluationCriterionService.updateCriterion(eq(1), any())).thenReturn(criterionResponse(1));

		mockMvc.perform(put("/api/evaluation_criteria/1")
						.contentType("application/json")
						.content("{\"criterionName\":\"Work attitude\",\"description\":\"Updated\","
								+ "\"maxScore\":10.00}"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.success").value(true))
				.andExpect(jsonPath("$.data.criterionId").value(1));
	}

	@Test
	void deleteCriterion_shouldReturnSuccessWithNullData() throws Exception {
		mockMvc.perform(delete("/api/evaluation_criteria/1"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.success").value(true))
				.andExpect(jsonPath("$.data").doesNotExist());

		verify(evaluationCriterionService).deleteCriterion(1);
	}

	private EvaluationCriterionResponse criterionResponse(Integer id) {
		return new EvaluationCriterionResponse(id, "Work attitude", "Evaluate responsibility",
				new BigDecimal("10.00"), null, null);
	}
}
