package com.example.internship_management.dto.response;

import com.example.internship_management.entity.Student;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StudentResponse {

	private Integer studentId;
	private String studentCode;
	private String major;
	private String className;
	private LocalDate dateOfBirth;
	private String address;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	private UserSummaryResponse user;

	public static StudentResponse from(Student student, UserSummaryResponse user) {
		return new StudentResponse(
				student.getStudentId(),
				student.getStudentCode(),
				student.getMajor(),
				student.getClassName(),
				student.getDateOfBirth(),
				student.getAddress(),
				student.getCreatedAt(),
				student.getUpdatedAt(),
				user);
	}
}
