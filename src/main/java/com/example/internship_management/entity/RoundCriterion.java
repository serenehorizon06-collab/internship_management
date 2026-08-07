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
@Table(name = "round_criteria")
public class RoundCriterion {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "round_criterion_id")
	private Integer roundCriterionId;

	@Column(name = "round_id", nullable = false)
	private Integer roundId;

	@Column(name = "criterion_id", nullable = false)
	private Integer criterionId;

	@Column(name = "weight", nullable = false, precision = 5, scale = 2)
	private BigDecimal weight;

	@Column(name = "created_at", nullable = false, insertable = false, updatable = false)
	private LocalDateTime createdAt;

	@Column(name = "updated_at", nullable = false, insertable = false, updatable = false)
	private LocalDateTime updatedAt;
}
