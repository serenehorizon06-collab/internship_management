package com.example.internship_management.service.impl;

import com.example.internship_management.dto.request.LoginRequest;
import com.example.internship_management.dto.response.AuthUserResponse;
import com.example.internship_management.dto.response.LoginResponse;
import com.example.internship_management.entity.User;
import com.example.internship_management.exception.ErrorCode;
import com.example.internship_management.exception.ForbiddenException;
import com.example.internship_management.exception.ResourceNotFoundException;
import com.example.internship_management.exception.UnauthorizedException;
import com.example.internship_management.repository.UserRepository;
import com.example.internship_management.security.CustomUserDetails;
import com.example.internship_management.security.JwtService;
import com.example.internship_management.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

	private static final String BAD_CREDENTIALS_MESSAGE = "Tên đăng nhập hoặc mật khẩu không đúng";

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtService jwtService;

	@Override
	public LoginResponse login(LoginRequest request) {
		User user = userRepository.findByUsername(request.getUsername())
				.orElseThrow(this::badCredentials);

		if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
			throw badCredentials();
		}

		if (!Boolean.TRUE.equals(user.getIsActive())) {
			throw new ForbiddenException("Tài khoản đã bị vô hiệu hóa");
		}

		return new LoginResponse(
				jwtService.generateToken(user),
				"Bearer",
				jwtService.getExpirationMs(),
				AuthUserResponse.from(user));
	}

	@Override
	public AuthUserResponse getCurrentUser(Authentication authentication) {
		if (authentication == null || !authentication.isAuthenticated()) {
			throw new UnauthorizedException(ErrorCode.INVALID_JWT_TOKEN, "Token xác thực không hợp lệ hoặc bị thiếu");
		}

		Object principal = authentication.getPrincipal();
		User user;
		if (principal instanceof CustomUserDetails userDetails) {
			user = userRepository.findById(userDetails.getUserId())
					.orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng"));
		} else {
			user = userRepository.findByUsername(authentication.getName())
					.orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng"));
		}

		return AuthUserResponse.from(user);
	}

	private UnauthorizedException badCredentials() {
		return new UnauthorizedException(ErrorCode.BAD_CREDENTIALS, BAD_CREDENTIALS_MESSAGE);
	}
}
