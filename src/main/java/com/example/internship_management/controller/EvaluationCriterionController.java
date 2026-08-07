package com.example.internship_management.controller;

import com.example.internship_management.common.ApiResponse;
import com.example.internship_management.dto.request.CreateEvaluationCriterionRequest;
import com.example.internship_management.dto.request.UpdateEvaluationCriterionRequest;
import com.example.internship_management.dto.response.EvaluationCriterionResponse;
import com.example.internship_management.service.EvaluationCriterionService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/evaluation_criteria")
@RequiredArgsConstructor
public class EvaluationCriterionController {

	private final EvaluationCriterionService evaluationCriterionService;

	@GetMapping
	public ResponseEntity<ApiResponse<List<EvaluationCriterionResponse>>> getCriteria() {
		return ResponseEntity.ok(ApiResponse.success(
				"Láº¥y danh sĂ¡ch tiĂªu chĂ­ Ä‘Ă¡nh giĂ¡ thĂ nh cĂ´ng",
				evaluationCriterionService.getAllCriteria()));
	}

	@GetMapping("/{criterionId}")
	public ResponseEntity<ApiResponse<EvaluationCriterionResponse>> getCriterionById(
			@PathVariable("criterionId") Integer criterionId) {
		return ResponseEntity.ok(ApiResponse.success(
				"Láº¥y thĂ´ng tin tiĂªu chĂ­ Ä‘Ă¡nh giĂ¡ thĂ nh cĂ´ng",
				evaluationCriterionService.getCriterionById(criterionId)));
	}

	@PostMapping
	public ResponseEntity<ApiResponse<EvaluationCriterionResponse>> createCriterion(
			@Valid @RequestBody CreateEvaluationCriterionRequest request) {
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(ApiResponse.created(
						"Táº¡o tiĂªu chĂ­ Ä‘Ă¡nh giĂ¡ thĂ nh cĂ´ng",
						evaluationCriterionService.createCriterion(request)));
	}

	@PutMapping("/{criterionId}")
	public ResponseEntity<ApiResponse<EvaluationCriterionResponse>> updateCriterion(
			@PathVariable("criterionId") Integer criterionId,
			@Valid @RequestBody UpdateEvaluationCriterionRequest request) {
		return ResponseEntity.ok(ApiResponse.success(
				"Cáº­p nháº­t tiĂªu chĂ­ Ä‘Ă¡nh giĂ¡ thĂ nh cĂ´ng",
				evaluationCriterionService.updateCriterion(criterionId, request)));
	}

	@DeleteMapping("/{criterionId}")
	public ResponseEntity<ApiResponse<Void>> deleteCriterion(@PathVariable("criterionId") Integer criterionId) {
		evaluationCriterionService.deleteCriterion(criterionId);
		return ResponseEntity.ok(ApiResponse.<Void>success("XĂ³a tiĂªu chĂ­ Ä‘Ă¡nh giĂ¡ thĂ nh cĂ´ng", null));
	}
}
