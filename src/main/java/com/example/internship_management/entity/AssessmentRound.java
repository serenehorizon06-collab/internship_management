package com.example.internship_management.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "assessment_rounds")
public class AssessmentRound {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "round_id")
	private Integer roundId;

	@Column(name = "phase_id", nullable = false)
	private Integer phaseId;

	@Column(name = "round_name", nullable = false, length = 100)
	private String roundName;

	@Column(name = "start_date", nullable = false)
	private LocalDate startDate;

	@Column(name = "end_date", nullable = false)
	private LocalDate endDate;

	@Column(name = "description", columnDefinition = "TEXT")
	private String description;

	@Column(name = "is_active", nullable = false)
	private Boolean isActive = true;

	@Column(name = "created_at", nullable = false, insertable = false, updatable = false)
	private LocalDateTime createdAt;

	@Column(name = "updated_at", nullable = false, insertable = false, updatable = false)
	private LocalDateTime updatedAt;
}
