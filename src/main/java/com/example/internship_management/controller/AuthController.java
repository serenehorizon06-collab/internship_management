package com.example.internship_management.controller;

import com.example.internship_management.common.ApiResponse;
import com.example.internship_management.dto.request.LoginRequest;
import com.example.internship_management.dto.response.AuthUserResponse;
import com.example.internship_management.dto.response.LoginResponse;
import com.example.internship_management.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

	private final AuthService authService;

	@PostMapping("/login")
	public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest request) {
		return ResponseEntity.ok(ApiResponse.success("Đăng nhập thành công", authService.login(request)));
	}

	@GetMapping("/me")
	public ResponseEntity<ApiResponse<AuthUserResponse>> getCurrentUser(Authentication authentication) {
		return ResponseEntity.ok(ApiResponse.success(
				"Lấy thông tin người dùng hiện tại thành công",
				authService.getCurrentUser(authentication)));
	}
}
