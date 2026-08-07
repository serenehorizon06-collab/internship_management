package com.example.internship_management.dto.request;

import com.example.internship_management.entity.UserRole;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUserRoleRequest {

	@NotNull(message = "Vai trò không được để trống")
	private UserRole role;
}
