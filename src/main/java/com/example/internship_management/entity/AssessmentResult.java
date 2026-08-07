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
@Table(name = "assessment_results")
public class AssessmentResult {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "result_id")
	private Integer resultId;

	@Column(name = "assignment_id", nullable = false)
	private Integer assignmentId;

	@Column(name = "round_id", nullable = false)
	private Integer roundId;

	@Column(name = "criterion_id", nullable = false)
	private Integer criterionId;

	@Column(name = "score", nullable = false, precision = 5, scale = 2)
	private BigDecimal score;

	@Column(name = "comments", columnDefinition = "TEXT")
	private String comments;

	@Column(name = "evaluated_by", nullable = false)
	private Integer evaluatedBy;

	@Column(name = "evaluation_date", nullable = false, insertable = false, updatable = false)
	private LocalDateTime evaluationDate;

	@Column(name = "created_at", nullable = false, insertable = false, updatable = false)
	private LocalDateTime createdAt;

	@Column(name = "updated_at", nullable = false, insertable = false, updatable = false)
	private LocalDateTime updatedAt;
}
