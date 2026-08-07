package com.example.internship_management.service;

import com.example.internship_management.dto.request.LoginRequest;
import com.example.internship_management.dto.response.AuthUserResponse;
import com.example.internship_management.dto.response.LoginResponse;
import org.springframework.security.core.Authentication;

public interface AuthService {

	LoginResponse login(LoginRequest request);

	AuthUserResponse getCurrentUser(Authentication authentication);
}
