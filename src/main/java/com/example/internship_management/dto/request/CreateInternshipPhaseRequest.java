package com.example.internship_management.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateInternshipPhaseRequest {

	@NotBlank(message = "Tên giai đoạn thực tập không được để trống")
	@Size(max = 100, message = "Tên giai đoạn thực tập không được vượt quá 100 ký tự")
	private String phaseName;

	@NotNull(message = "Ngày bắt đầu không được để trống")
	private LocalDate startDate;

	@NotNull(message = "Ngày kết thúc không được để trống")
	private LocalDate endDate;

	private String description;
}
