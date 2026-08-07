package com.example.internship_management.dto.request;

import com.example.internship_management.entity.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateUserRequest {

	@NotBlank(message = "Tên đăng nhập không được để trống")
	@Size(max = 50, message = "Tên đăng nhập không được vượt quá 50 ký tự")
	private String username;

	@NotBlank(message = "Mật khẩu không được để trống")
	@Size(min = 6, message = "Mật khẩu phải có ít nhất 6 ký tự")
	private String password;

	@NotBlank(message = "Họ tên không được để trống")
	@Size(max = 100, message = "Họ tên không được vượt quá 100 ký tự")
	private String fullName;

	@NotBlank(message = "Email không được để trống")
	@Email(message = "Email không hợp lệ")
	@Size(max = 100, message = "Email không được vượt quá 100 ký tự")
	private String email;

	@Size(max = 20, message = "Số điện thoại không được vượt quá 20 ký tự")
	private String phoneNumber;

	@NotNull(message = "Vai trò không được để trống")
	private UserRole role;

	private Boolean isActive;
}
