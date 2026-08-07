package com.example.internship_management.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateStudentRequest {

	@NotBlank(message = "Mã sinh viên không được để trống")
	@Size(max = 20, message = "Mã sinh viên không được vượt quá 20 ký tự")
	private String studentCode;

	@Size(max = 100, message = "Chuyên ngành không được vượt quá 100 ký tự")
	private String major;

	@Size(max = 50, message = "Tên lớp không được vượt quá 50 ký tự")
	private String className;

	private LocalDate dateOfBirth;

	@Size(max = 255, message = "Địa chỉ không được vượt quá 255 ký tự")
	private String address;
}
