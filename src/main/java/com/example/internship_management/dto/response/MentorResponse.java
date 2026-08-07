package com.example.internship_management.dto.response;

import com.example.internship_management.entity.Mentor;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MentorResponse {

	private Integer mentorId;
	private String department;
	private String academicRank;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	private UserSummaryResponse user;

	public static MentorResponse from(Mentor mentor, UserSummaryResponse user) {
		return new MentorResponse(
				mentor.getMentorId(),
				mentor.getDepartment(),
				mentor.getAcademicRank(),
				mentor.getCreatedAt(),
				mentor.getUpdatedAt(),
				user);
	}
}
