package com.example.internship_management.controller;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.internship_management.dto.response.UserResponse;
import com.example.internship_management.entity.User;
import com.example.internship_management.entity.UserRole;
import com.example.internship_management.exception.ForbiddenException;
import com.example.internship_management.exception.GlobalExceptionHandler;
import com.example.internship_management.exception.ResourceNotFoundException;
import com.example.internship_management.security.CustomUserDetails;
import com.example.internship_management.service.UserService;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
class UserControllerTests {

	private MockMvc mockMvc;

	@Mock
	private UserService userService;

	@BeforeEach
	void setUp() {
		mockMvc = MockMvcBuilders.standaloneSetup(new UserController(userService))
				.setControllerAdvice(new GlobalExceptionHandler())
				.build();
	}

	@Test
	void getUsers_shouldReturnUserList() throws Exception {
		when(userService.getAllUsers(null)).thenReturn(List.of(userResponse(1, "admin", UserRole.ADMIN)));

		mockMvc.perform(get("/api/users"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.success").value(true))
				.andExpect(jsonPath("$.status_code").value(200))
				.andExpect(jsonPath("$.data[0].username").value("admin"))
				.andExpect(jsonPath("$.timestamp").exists())
				.andExpect(content().string(not(containsString("passwordHash"))))
				.andExpect(content().string(not(containsString("password_hash"))));
	}

	@Test
	void getUsers_shouldPassRoleFilterToService() throws Exception {
		when(userService.getAllUsers(UserRole.STUDENT)).thenReturn(List.of(userResponse(2, "student", UserRole.STUDENT)));

		mockMvc.perform(get("/api/users").param("role", "STUDENT"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.success").value(true))
				.andExpect(jsonPath("$.data[0].role").value("STUDENT"));
	}

	@Test
	void getUserById_shouldReturnUserDetail() throws Exception {
		when(userService.getUserById(1)).thenReturn(userResponse(1, "admin", UserRole.ADMIN));

		mockMvc.perform(get("/api/users/1"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.success").value(true))
				.andExpect(jsonPath("$.data.userId").value(1))
				.andExpect(content().string(not(containsString("passwordHash"))));
	}

	@Test
	void getUserById_shouldReturnNotFound_whenUserMissing() throws Exception {
		when(userService.getUserById(99)).thenThrow(new ResourceNotFoundException("Không tìm thấy người dùng"));

		mockMvc.perform(get("/api/users/99"))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.success").value(false))
				.andExpect(jsonPath("$.status_code").value(404))
				.andExpect(jsonPath("$.error_code").value("RESOURCE_NOT_FOUND"));
	}

	@Test
	void createUser_shouldReturnCreatedUserWithoutPassword() throws Exception {
		when(userService.createUser(any())).thenReturn(userResponse(2, "student", UserRole.STUDENT));

		mockMvc.perform(post("/api/users")
						.contentType("application/json")
						.content("{\"username\":\"student\",\"password\":\"student123\",\"fullName\":\"Student User\","
								+ "\"email\":\"student@example.com\",\"role\":\"STUDENT\",\"isActive\":true}"))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.success").value(true))
				.andExpect(jsonPath("$.status_code").value(201))
				.andExpect(jsonPath("$.data.username").value("student"))
				.andExpect(content().string(not(containsString("password"))));
	}

	@Test
	void updateUser_shouldReturnUpdatedUser() throws Exception {
		when(userService.updateUser(eq(1), any())).thenReturn(userResponse(1, "admin2", UserRole.ADMIN));

		mockMvc.perform(put("/api/users/1")
						.contentType("application/json")
						.content("{\"username\":\"admin2\",\"fullName\":\"Admin Two\",\"email\":\"admin2@example.com\"}"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.success").value(true))
				.andExpect(jsonPath("$.data.username").value("admin2"));
	}

	@Test
	void updateUserStatus_shouldReturnUpdatedStatus() throws Exception {
		when(userService.updateUserStatus(eq(1), any())).thenReturn(userResponse(1, "admin", UserRole.ADMIN));

		mockMvc.perform(put("/api/users/1/status")
						.contentType("application/json")
						.content("{\"isActive\":false}"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.success").value(true))
				.andExpect(jsonPath("$.data.userId").value(1));
	}

	@Test
	void updateUserRole_shouldReturnUpdatedRole() throws Exception {
		when(userService.updateUserRole(eq(2), any(), eq(1))).thenReturn(userResponse(2, "mentor", UserRole.MENTOR));

		mockMvc.perform(put("/api/users/2/role")
						.principal(new TestingAuthenticationToken(adminPrincipal(), null))
						.contentType("application/json")
						.content("{\"role\":\"MENTOR\"}"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.success").value(true))
				.andExpect(jsonPath("$.data.role").value("MENTOR"));
	}

	@Test
	void updateUserRole_shouldReturnAccessDenied_whenChangingAnotherAdminRole() throws Exception {
		when(userService.updateUserRole(eq(2), any(), eq(1)))
				.thenThrow(new ForbiddenException("Không được thay đổi quyền của ADMIN khác"));

		mockMvc.perform(put("/api/users/2/role")
						.principal(new TestingAuthenticationToken(adminPrincipal(), null))
						.contentType("application/json")
						.content("{\"role\":\"STUDENT\"}"))
				.andExpect(status().isForbidden())
				.andExpect(jsonPath("$.success").value(false))
				.andExpect(jsonPath("$.error_code").value("ACCESS_DENIED"));
	}

	@Test
	void deleteUser_shouldReturnSuccessWithNullData() throws Exception {
		mockMvc.perform(delete("/api/users/2"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.success").value(true))
				.andExpect(jsonPath("$.status_code").value(200))
				.andExpect(jsonPath("$.data").doesNotExist());

		verify(userService).deleteUser(2);
	}

	private UserResponse userResponse(Integer id, String username, UserRole role) {
		return new UserResponse(id, username, username + " Full", username + "@example.com", null, role, true, null, null);
	}

	private CustomUserDetails adminPrincipal() {
		User user = new User();
		user.setUserId(1);
		user.setUsername("admin");
		user.setPasswordHash("$2a$10$hash");
		user.setRole(UserRole.ADMIN);
		user.setIsActive(true);
		return new CustomUserDetails(user);
	}
}
