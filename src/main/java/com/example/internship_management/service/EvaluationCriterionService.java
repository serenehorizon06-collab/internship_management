package com.example.internship_management.service;

import com.example.internship_management.dto.request.CreateEvaluationCriterionRequest;
import com.example.internship_management.dto.request.UpdateEvaluationCriterionRequest;
import com.example.internship_management.dto.response.EvaluationCriterionResponse;
import java.util.List;

public interface EvaluationCriterionService {

	List<EvaluationCriterionResponse> getAllCriteria();

	EvaluationCriterionResponse getCriterionById(Integer criterionId);

	EvaluationCriterionResponse createCriterion(CreateEvaluationCriterionRequest request);

	EvaluationCriterionResponse updateCriterion(Integer criterionId, UpdateEvaluationCriterionRequest request);

	void deleteCriterion(Integer criterionId);
}
