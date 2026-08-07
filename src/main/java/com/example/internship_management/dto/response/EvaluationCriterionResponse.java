package com.example.internship_management.dto.response;

import com.example.internship_management.entity.EvaluationCriterion;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EvaluationCriterionResponse {

	private Integer criterionId;
	private String criterionName;
	private String description;
	private BigDecimal maxScore;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	public static EvaluationCriterionResponse from(EvaluationCriterion criterion) {
		return new EvaluationCriterionResponse(
				criterion.getCriterionId(),
				criterion.getCriterionName(),
				criterion.getDescription(),
				criterion.getMaxScore(),
				criterion.getCreatedAt(),
				criterion.getUpdatedAt());
	}
}
