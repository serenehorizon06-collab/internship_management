package com.example.internship_management.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUserStatusRequest {

	@NotNull(message = "Trạng thái tài khoản không được để trống")
	private Boolean isActive;
}
