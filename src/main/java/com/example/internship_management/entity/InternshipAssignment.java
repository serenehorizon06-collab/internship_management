package com.example.internship_management.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "internship_assignments")
public class InternshipAssignment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "assignment_id")
	private Integer assignmentId;

	@Column(name = "student_id", nullable = false)
	private Integer studentId;

	@Column(name = "mentor_id", nullable = false)
	private Integer mentorId;

	@Column(name = "phase_id", nullable = false)
	private Integer phaseId;

	@Column(name = "assigned_date", nullable = false, insertable = false, updatable = false)
	private LocalDateTime assignedDate;

	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false, columnDefinition = "assignment_status")
	private AssignmentStatus status = AssignmentStatus.PENDING;

	@Column(name = "created_at", nullable = false, insertable = false, updatable = false)
	private LocalDateTime createdAt;

	@Column(name = "updated_at", nullable = false, insertable = false, updatable = false)
	private LocalDateTime updatedAt;
}
