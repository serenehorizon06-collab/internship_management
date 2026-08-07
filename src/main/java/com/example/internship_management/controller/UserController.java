package com.example.internship_management.controller;

import com.example.internship_management.common.ApiResponse;
import com.example.internship_management.dto.request.CreateUserRequest;
import com.example.internship_management.dto.request.UpdateUserRequest;
import com.example.internship_management.dto.request.UpdateUserRoleRequest;
import com.example.internship_management.dto.request.UpdateUserStatusRequest;
import com.example.internship_management.dto.response.UserResponse;
import com.example.internship_management.entity.UserRole;
import com.example.internship_management.exception.InvalidInputException;
import com.example.internship_management.exception.UnauthorizedException;
import com.example.internship_management.security.CustomUserDetails;
import com.example.internship_management.service.UserService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	@GetMapping
	public ResponseEntity<ApiResponse<List<UserResponse>>> getAllUsers(@RequestParam(required = false) String role) {
		List<UserResponse> users = userService.getAllUsers(parseRole(role));
		return ResponseEntity.ok(ApiResponse.success("Lấy danh sách người dùng thành công", users));
	}

	@GetMapping("/{userId}")
	public ResponseEntity<ApiResponse<UserResponse>> getUserById(@PathVariable("userId") Integer userId) {
		return ResponseEntity.ok(ApiResponse.success(
				"Lấy thông tin người dùng thành công",
				userService.getUserById(userId)));
	}

	@PostMapping
	public ResponseEntity<ApiResponse<UserResponse>> createUser(@Valid @RequestBody CreateUserRequest request) {
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(ApiResponse.created("Tạo người dùng thành công", userService.createUser(request)));
	}

	@PutMapping("/{userId}")
	public ResponseEntity<ApiResponse<UserResponse>> updateUser(
			@PathVariable("userId") Integer userId,
			@Valid @RequestBody UpdateUserRequest request) {
		return ResponseEntity.ok(ApiResponse.success(
				"Cập nhật người dùng thành công",
				userService.updateUser(userId, request)));
	}

	@PutMapping("/{userId}/status")
	public ResponseEntity<ApiResponse<UserResponse>> updateUserStatus(
			@PathVariable("userId") Integer userId,
			@Valid @RequestBody UpdateUserStatusRequest request) {
		return ResponseEntity.ok(ApiResponse.success(
				"Cập nhật trạng thái người dùng thành công",
				userService.updateUserStatus(userId, request)));
	}

	@PutMapping("/{userId}/role")
	public ResponseEntity<ApiResponse<UserResponse>> updateUserRole(
			@PathVariable("userId") Integer userId,
			@Valid @RequestBody UpdateUserRoleRequest request,
			Authentication authentication) {
		return ResponseEntity.ok(ApiResponse.success(
				"Cập nhật vai trò người dùng thành công",
				userService.updateUserRole(userId, request, getCurrentUserId(authentication))));
	}

	@DeleteMapping("/{userId}")
	public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable("userId") Integer userId) {
		userService.deleteUser(userId);
		return ResponseEntity.ok(ApiResponse.<Void>success("Xóa người dùng thành công", null));
	}

	private UserRole parseRole(String role) {
		if (role == null || role.isBlank()) {
			return null;
		}

		try {
			return UserRole.valueOf(role);
		} catch (IllegalArgumentException exception) {
			throw new InvalidInputException("Vai trò không hợp lệ");
		}
	}

	private Integer getCurrentUserId(Authentication authentication) {
		if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails userDetails) {
			return userDetails.getUserId();
		}
		throw new UnauthorizedException("Token xác thực không hợp lệ hoặc bị thiếu");
	}
}
