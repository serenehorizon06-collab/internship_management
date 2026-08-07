package com.example.internship_management.dto.response;

import com.example.internship_management.entity.User;
import com.example.internship_management.entity.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthUserResponse {

	private Integer userId;
	private String username;
	private String fullName;
	private String email;
	private String phoneNumber;
	private UserRole role;
	private Boolean isActive;

	public static AuthUserResponse from(User user) {
		return new AuthUserResponse(
				user.getUserId(),
				user.getUsername(),
				user.getFullName(),
				user.getEmail(),
				user.getPhoneNumber(),
				user.getRole(),
				user.getIsActive());
	}
}
