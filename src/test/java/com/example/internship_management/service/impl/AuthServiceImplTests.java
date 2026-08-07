package com.example.internship_management.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.example.internship_management.dto.request.LoginRequest;
import com.example.internship_management.exception.ErrorCode;
import com.example.internship_management.exception.UnauthorizedException;
import com.example.internship_management.repository.UserRepository;
import com.example.internship_management.security.JwtService;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

class AuthServiceImplTests {

	@Test
	void loginWithUnknownUsernameThrowsBadCredentials() {
		UserRepository userRepository = mock(UserRepository.class);
		PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
		JwtService jwtService = mock(JwtService.class);
		AuthServiceImpl authService = new AuthServiceImpl(userRepository, passwordEncoder, jwtService);
		LoginRequest request = new LoginRequest();
		request.setUsername("missing");
		request.setPassword("wrong");
		when(userRepository.findByUsername("missing")).thenReturn(Optional.empty());

		UnauthorizedException exception = assertThrows(UnauthorizedException.class, () -> authService.login(request));

		assertEquals(ErrorCode.BAD_CREDENTIALS, exception.getErrorCode());
		assertEquals("Tên đăng nhập hoặc mật khẩu không đúng", exception.getMessage());
	}
}
