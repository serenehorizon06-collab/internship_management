package com.example.internship_management.dto.request;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateMentorRequest {

	@Size(max = 100, message = "Bộ môn không được vượt quá 100 ký tự")
	private String department;

	@Size(max = 50, message = "Học hàm/học vị không được vượt quá 50 ký tự")
	private String academicRank;
}
