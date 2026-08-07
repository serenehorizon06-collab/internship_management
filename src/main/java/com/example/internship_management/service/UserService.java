package com.example.internship_management.service;

import com.example.internship_management.dto.request.CreateUserRequest;
import com.example.internship_management.dto.request.UpdateUserRequest;
import com.example.internship_management.dto.request.UpdateUserRoleRequest;
import com.example.internship_management.dto.request.UpdateUserStatusRequest;
import com.example.internship_management.dto.response.UserResponse;
import com.example.internship_management.entity.UserRole;
import java.util.List;

public interface UserService {

	List<UserResponse> getAllUsers(UserRole roleFilter);

	UserResponse getUserById(Integer userId);

	UserResponse createUser(CreateUserRequest request);

	UserResponse updateUser(Integer userId, UpdateUserRequest request);

	UserResponse updateUserStatus(Integer userId, UpdateUserStatusRequest request);

	UserResponse updateUserRole(Integer userId, UpdateUserRoleRequest request, Integer currentAdminId);

	void deleteUser(Integer userId);
}
