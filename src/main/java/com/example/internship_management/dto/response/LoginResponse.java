package com.example.internship_management.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponse {

	private String accessToken;
	private String tokenType;
	private Long expiresIn;
	private AuthUserResponse user;
}
