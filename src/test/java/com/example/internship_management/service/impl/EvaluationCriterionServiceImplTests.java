package com.example.internship_management.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.example.internship_management.dto.request.CreateEvaluationCriterionRequest;
import com.example.internship_management.dto.request.UpdateEvaluationCriterionRequest;
import com.example.internship_management.entity.EvaluationCriterion;
import com.example.internship_management.exception.DuplicateResourceException;
import com.example.internship_management.exception.ErrorCode;
import com.example.internship_management.exception.InvalidInputException;
import com.example.internship_management.exception.ResourceNotFoundException;
import com.example.internship_management.repository.EvaluationCriterionRepository;
import java.math.BigDecimal;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class EvaluationCriterionServiceImplTests {

	private final EvaluationCriterionRepository evaluationCriterionRepository = mock(EvaluationCriterionRepository.class);
	private final EvaluationCriterionServiceImpl evaluationCriterionService =
			new EvaluationCriterionServiceImpl(evaluationCriterionRepository);

	@Test
	void createCriterionRejectsMaxScoreLessThanOrEqualZero() {
		CreateEvaluationCriterionRequest request = createRequest();
		request.setMaxScore(BigDecimal.ZERO);

		InvalidInputException exception = assertThrows(
				InvalidInputException.class,
				() -> evaluationCriterionService.createCriterion(request));

		assertEquals(ErrorCode.INVALID_INPUT_DATA, exception.getErrorCode());
		assertEquals("Điểm tối đa phải lớn hơn 0", exception.getMessage());
	}

	@Test
	void createCriterionRejectsDuplicateCriterionName() {
		CreateEvaluationCriterionRequest request = createRequest();
		when(evaluationCriterionRepository.existsByCriterionName("Thái độ làm việc")).thenReturn(true);

		DuplicateResourceException exception = assertThrows(
				DuplicateResourceException.class,
				() -> evaluationCriterionService.createCriterion(request));

		assertEquals(ErrorCode.DUPLICATE_RESOURCE, exception.getErrorCode());
	}

	@Test
	void updateCriterionRejectsDuplicateCriterionNameExcludingCurrentCriterion() {
		UpdateEvaluationCriterionRequest request = updateRequest();
		when(evaluationCriterionRepository.findById(1)).thenReturn(Optional.of(criterion(1)));
		when(evaluationCriterionRepository.existsByCriterionNameAndCriterionIdNot("Kiến thức chuyên môn", 1))
				.thenReturn(true);

		DuplicateResourceException exception = assertThrows(
				DuplicateResourceException.class,
				() -> evaluationCriterionService.updateCriterion(1, request));

		assertEquals(ErrorCode.DUPLICATE_RESOURCE, exception.getErrorCode());
	}

	@Test
	void getCriterionByMissingIdThrowsResourceNotFound() {
		when(evaluationCriterionRepository.findById(99)).thenReturn(Optional.empty());

		ResourceNotFoundException exception = assertThrows(
				ResourceNotFoundException.class,
				() -> evaluationCriterionService.getCriterionById(99));

		assertEquals(ErrorCode.RESOURCE_NOT_FOUND, exception.getErrorCode());
	}

	private CreateEvaluationCriterionRequest createRequest() {
		CreateEvaluationCriterionRequest request = new CreateEvaluationCriterionRequest();
		request.setCriterionName("Thái độ làm việc");
		request.setDescription("Đánh giá thái độ và trách nhiệm");
		request.setMaxScore(new BigDecimal("10.00"));
		return request;
	}

	private UpdateEvaluationCriterionRequest updateRequest() {
		UpdateEvaluationCriterionRequest request = new UpdateEvaluationCriterionRequest();
		request.setCriterionName("Kiến thức chuyên môn");
		request.setDescription("Đánh giá kiến thức chuyên môn");
		request.setMaxScore(new BigDecimal("10.00"));
		return request;
	}

	private EvaluationCriterion criterion(Integer criterionId) {
		EvaluationCriterion criterion = new EvaluationCriterion();
		criterion.setCriterionId(criterionId);
		criterion.setCriterionName("Thái độ làm việc");
		criterion.setDescription("Đánh giá thái độ và trách nhiệm");
		criterion.setMaxScore(new BigDecimal("10.00"));
		return criterion;
	}
}
