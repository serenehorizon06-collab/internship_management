package com.example.internship_management.controller;

import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.internship_management.dto.response.AuthUserResponse;
import com.example.internship_management.dto.response.LoginResponse;
import com.example.internship_management.entity.UserRole;
import com.example.internship_management.exception.ErrorCode;
import com.example.internship_management.exception.GlobalExceptionHandler;
import com.example.internship_management.exception.UnauthorizedException;
import com.example.internship_management.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
class AuthControllerTests {

	private MockMvc mockMvc;

	@Mock
	private AuthService authService;

	@BeforeEach
	void setUp() {
		mockMvc = MockMvcBuilders.standaloneSetup(new AuthController(authService))
				.setControllerAdvice(new GlobalExceptionHandler())
				.build();
	}

	@Test
	void login_shouldReturnToken_whenCredentialsAreValid() throws Exception {
		AuthUserResponse user = new AuthUserResponse(1, "admin", "Admin User", "admin@example.com", null,
				UserRole.ADMIN, true);
		when(authService.login(any())).thenReturn(new LoginResponse("access-token", "Bearer", 86400000L, user));

		mockMvc.perform(post("/api/auth/login")
						.contentType("application/json")
						.content("{\"username\":\"admin\",\"password\":\"admin123\"}"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.success").value(true))
				.andExpect(jsonPath("$.status_code").value(200))
				.andExpect(jsonPath("$.message").exists())
				.andExpect(jsonPath("$.data.accessToken").value("access-token"))
				.andExpect(jsonPath("$.timestamp").exists())
				.andExpect(content().string(not(containsString("passwordHash"))));
	}

	@Test
	void login_shouldReturnBadCredentials_whenPasswordIsWrong() throws Exception {
		when(authService.login(any()))
				.thenThrow(new UnauthorizedException(ErrorCode.BAD_CREDENTIALS, "Tên đăng nhập hoặc mật khẩu không đúng"));

		mockMvc.perform(post("/api/auth/login")
						.contentType("application/json")
						.content("{\"username\":\"admin\",\"password\":\"wrong\"}"))
				.andExpect(status().isUnauthorized())
				.andExpect(jsonPath("$.success").value(false))
				.andExpect(jsonPath("$.status_code").value(401))
				.andExpect(jsonPath("$.error_code").value("BAD_CREDENTIALS"))
				.andExpect(jsonPath("$.errors").isArray())
				.andExpect(jsonPath("$.timestamp").exists());
	}

	@Test
	void login_shouldReturnInvalidInput_whenRequiredFieldsAreMissing() throws Exception {
		mockMvc.perform(post("/api/auth/login")
						.contentType("application/json")
						.content("{}"))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.success").value(false))
				.andExpect(jsonPath("$.status_code").value(400))
				.andExpect(jsonPath("$.error_code").value("INVALID_INPUT_DATA"))
				.andExpect(jsonPath("$.errors").isArray());
	}

	@Test
	void getCurrentUser_shouldReturnCurrentUser_whenAuthenticated() throws Exception {
		when(authService.getCurrentUser(any()))
				.thenReturn(new AuthUserResponse(1, "admin", "Admin User", "admin@example.com", null,
						UserRole.ADMIN, true));

		mockMvc.perform(get("/api/auth/me")
						.principal(new TestingAuthenticationToken("admin", null)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.success").value(true))
				.andExpect(jsonPath("$.status_code").value(200))
				.andExpect(jsonPath("$.data.username").value("admin"))
				.andExpect(jsonPath("$.timestamp").exists())
				.andExpect(content().string(not(containsString("passwordHash"))))
				.andExpect(content().string(not(containsString("password_hash"))));
	}
}
