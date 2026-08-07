package com.example.internship_management.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "evaluation_criteria")
public class EvaluationCriterion {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "criterion_id")
	private Integer criterionId;

	@Column(name = "criterion_name", nullable = false, unique = true, length = 200)
	private String criterionName;

	@Column(name = "description", columnDefinition = "TEXT")
	private String description;

	@Column(name = "max_score", nullable = false, precision = 5, scale = 2)
	private BigDecimal maxScore;

	@Column(name = "created_at", nullable = false, insertable = false, updatable = false)
	private LocalDateTime createdAt;

	@Column(name = "updated_at", nullable = false, insertable = false, updatable = false)
	private LocalDateTime updatedAt;
}
