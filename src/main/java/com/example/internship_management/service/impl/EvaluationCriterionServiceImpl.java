package com.example.internship_management.service.impl;

import com.example.internship_management.dto.request.CreateEvaluationCriterionRequest;
import com.example.internship_management.dto.request.UpdateEvaluationCriterionRequest;
import com.example.internship_management.dto.response.EvaluationCriterionResponse;
import com.example.internship_management.entity.EvaluationCriterion;
import com.example.internship_management.exception.DuplicateResourceException;
import com.example.internship_management.exception.InvalidInputException;
import com.example.internship_management.exception.ResourceNotFoundException;
import com.example.internship_management.repository.EvaluationCriterionRepository;
import com.example.internship_management.service.EvaluationCriterionService;
import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EvaluationCriterionServiceImpl implements EvaluationCriterionService {

	private final EvaluationCriterionRepository evaluationCriterionRepository;

	@Override
	@Transactional(readOnly = true)
	public List<EvaluationCriterionResponse> getAllCriteria() {
		return evaluationCriterionRepository.findAll()
				.stream()
				.map(EvaluationCriterionResponse::from)
				.toList();
	}

	@Override
	@Transactional(readOnly = true)
	public EvaluationCriterionResponse getCriterionById(Integer criterionId) {
		return EvaluationCriterionResponse.from(findCriterionById(criterionId));
	}

	@Override
	@Transactional
	public EvaluationCriterionResponse createCriterion(CreateEvaluationCriterionRequest request) {
		validateMaxScore(request.getMaxScore());
		validateNewCriterionName(request.getCriterionName());

		EvaluationCriterion criterion = new EvaluationCriterion();
		criterion.setCriterionName(request.getCriterionName());
		criterion.setDescription(request.getDescription());
		criterion.setMaxScore(request.getMaxScore());

		return EvaluationCriterionResponse.from(evaluationCriterionRepository.save(criterion));
	}

	@Override
	@Transactional
	public EvaluationCriterionResponse updateCriterion(Integer criterionId, UpdateEvaluationCriterionRequest request) {
		EvaluationCriterion criterion = findCriterionById(criterionId);
		validateMaxScore(request.getMaxScore());
		validateCriterionNameForUpdate(request.getCriterionName(), criterionId);

		criterion.setCriterionName(request.getCriterionName());
		criterion.setDescription(request.getDescription());
		criterion.setMaxScore(request.getMaxScore());

		return EvaluationCriterionResponse.from(evaluationCriterionRepository.save(criterion));
	}

	@Override
	@Transactional
	public void deleteCriterion(Integer criterionId) {
		EvaluationCriterion criterion = findCriterionById(criterionId);
		evaluationCriterionRepository.delete(criterion);
	}

	private EvaluationCriterion findCriterionById(Integer criterionId) {
		return evaluationCriterionRepository.findById(criterionId)
				.orElseThrow(() -> new ResourceNotFoundException("KhĂ´ng tĂ¬m tháº¥y tiĂªu chĂ­ Ä‘Ă¡nh giĂ¡"));
	}

	private void validateNewCriterionName(String criterionName) {
		if (evaluationCriterionRepository.existsByCriterionName(criterionName)) {
			throw new DuplicateResourceException("TĂªn tiĂªu chĂ­ Ä‘Ă¡nh giĂ¡ Ä‘Ă£ tá»“n táº¡i");
		}
	}

	private void validateCriterionNameForUpdate(String criterionName, Integer criterionId) {
		if (evaluationCriterionRepository.existsByCriterionNameAndCriterionIdNot(criterionName, criterionId)) {
			throw new DuplicateResourceException("TĂªn tiĂªu chĂ­ Ä‘Ă¡nh giĂ¡ Ä‘Ă£ tá»“n táº¡i");
		}
	}

	private void validateMaxScore(BigDecimal maxScore) {
		if (maxScore != null && maxScore.compareTo(BigDecimal.ZERO) <= 0) {
			throw new InvalidInputException("Äiá»ƒm tá»‘i Ä‘a pháº£i lá»›n hÆ¡n 0");
		}
	}
}
