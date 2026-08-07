package com.example.internship_management.dto.response;

import com.example.internship_management.entity.InternshipPhase;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class InternshipPhaseResponse {

	private Integer phaseId;
	private String phaseName;
	private LocalDate startDate;
	private LocalDate endDate;
	private String description;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	public static InternshipPhaseResponse from(InternshipPhase phase) {
		return new InternshipPhaseResponse(
				phase.getPhaseId(),
				phase.getPhaseName(),
				phase.getStartDate(),
				phase.getEndDate(),
				phase.getDescription(),
				phase.getCreatedAt(),
				phase.getUpdatedAt());
	}
}
