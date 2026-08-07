package com.example.internship_management.service.impl;

import com.example.internship_management.dto.request.CreateUserRequest;
import com.example.internship_management.dto.request.UpdateUserRequest;
import com.example.internship_management.dto.request.UpdateUserRoleRequest;
import com.example.internship_management.dto.request.UpdateUserStatusRequest;
import com.example.internship_management.dto.response.UserResponse;
import com.example.internship_management.entity.User;
import com.example.internship_management.entity.UserRole;
import com.example.internship_management.exception.DuplicateResourceException;
import com.example.internship_management.exception.ForbiddenException;
import com.example.internship_management.exception.ResourceNotFoundException;
import com.example.internship_management.repository.UserRepository;
import com.example.internship_management.service.UserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	@Override
	@Transactional(readOnly = true)
	public List<UserResponse> getAllUsers(UserRole roleFilter) {
		List<User> users = roleFilter == null ? userRepository.findAll() : userRepository.findByRole(roleFilter);
		return users.stream()
				.map(UserResponse::from)
				.toList();
	}

	@Override
	@Transactional(readOnly = true)
	public UserResponse getUserById(Integer userId) {
		return UserResponse.from(findUserById(userId));
	}

	@Override
	@Transactional
	public UserResponse createUser(CreateUserRequest request) {
		validateNewUsername(request.getUsername());
		validateNewEmail(request.getEmail());

		User user = new User();
		user.setUsername(request.getUsername());
		user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
		user.setFullName(request.getFullName());
		user.setEmail(request.getEmail());
		user.setPhoneNumber(request.getPhoneNumber());
		user.setRole(request.getRole());
		user.setIsActive(request.getIsActive() == null ? true : request.getIsActive());

		return UserResponse.from(userRepository.save(user));
	}

	@Override
	@Transactional
	public UserResponse updateUser(Integer userId, UpdateUserRequest request) {
		User user = findUserById(userId);
		validateUsernameForUpdate(request.getUsername(), userId);
		validateEmailForUpdate(request.getEmail(), userId);

		user.setUsername(request.getUsername());
		user.setFullName(request.getFullName());
		user.setEmail(request.getEmail());
		user.setPhoneNumber(request.getPhoneNumber());

		return UserResponse.from(userRepository.save(user));
	}

	@Override
	@Transactional
	public UserResponse updateUserStatus(Integer userId, UpdateUserStatusRequest request) {
		User user = findUserById(userId);
		user.setIsActive(request.getIsActive());
		return UserResponse.from(userRepository.save(user));
	}

	@Override
	@Transactional
	public UserResponse updateUserRole(Integer userId, UpdateUserRoleRequest request, Integer currentAdminId) {
		User user = findUserById(userId);
		if (UserRole.ADMIN.equals(user.getRole()) && !user.getUserId().equals(currentAdminId)) {
			throw new ForbiddenException("Không được thay đổi quyền của ADMIN khác");
		}

		user.setRole(request.getRole());
		return UserResponse.from(userRepository.save(user));
	}

	@Override
	@Transactional
	public void deleteUser(Integer userId) {
		User user = findUserById(userId);
		userRepository.delete(user);
	}

	private User findUserById(Integer userId) {
		return userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng"));
	}

	private void validateNewUsername(String username) {
		if (userRepository.existsByUsername(username)) {
			throw new DuplicateResourceException("Username đã tồn tại");
		}
	}

	private void validateNewEmail(String email) {
		if (userRepository.existsByEmail(email)) {
			throw new DuplicateResourceException("Email đã tồn tại");
		}
	}

	private void validateUsernameForUpdate(String username, Integer userId) {
		if (userRepository.existsByUsernameAndUserIdNot(username, userId)) {
			throw new DuplicateResourceException("Username đã tồn tại");
		}
	}

	private void validateEmailForUpdate(String email, Integer userId) {
		if (userRepository.existsByEmailAndUserIdNot(email, userId)) {
			throw new DuplicateResourceException("Email đã tồn tại");
		}
	}
}
