package com.example.internship_management.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.internship_management.dto.request.CreateUserRequest;
import com.example.internship_management.dto.request.UpdateUserRoleRequest;
import com.example.internship_management.entity.User;
import com.example.internship_management.entity.UserRole;
import com.example.internship_management.exception.DuplicateResourceException;
import com.example.internship_management.exception.ErrorCode;
import com.example.internship_management.exception.ForbiddenException;
import com.example.internship_management.repository.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

class UserServiceImplTests {

	@Test
	void createUserEncodesPassword() {
		UserRepository userRepository = mock(UserRepository.class);
		PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		UserServiceImpl userService = new UserServiceImpl(userRepository, passwordEncoder);
		CreateUserRequest request = createUserRequest();
		ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
		when(userRepository.existsByUsername("mentor01")).thenReturn(false);
		when(userRepository.existsByEmail("mentor@example.com")).thenReturn(false);
		when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

		userService.createUser(request);

		verify(userRepository).save(userCaptor.capture());
		User savedUser = userCaptor.getValue();
		assertNotEquals("123456", savedUser.getPasswordHash());
		assertTrue(passwordEncoder.matches("123456", savedUser.getPasswordHash()));
	}

	@Test
	void createUserWithDuplicateUsernameThrowsDuplicateResource() {
		UserRepository userRepository = mock(UserRepository.class);
		UserServiceImpl userService = new UserServiceImpl(userRepository, mock(PasswordEncoder.class));
		CreateUserRequest request = createUserRequest();
		when(userRepository.existsByUsername("mentor01")).thenReturn(true);

		DuplicateResourceException exception = assertThrows(
				DuplicateResourceException.class,
				() -> userService.createUser(request));

		assertEquals(ErrorCode.DUPLICATE_RESOURCE, exception.getErrorCode());
	}

	@Test
	void updateRoleOfAnotherAdminThrowsAccessDenied() {
		UserRepository userRepository = mock(UserRepository.class);
		UserServiceImpl userService = new UserServiceImpl(userRepository, mock(PasswordEncoder.class));
		User targetAdmin = new User();
		targetAdmin.setUserId(2);
		targetAdmin.setRole(UserRole.ADMIN);
		UpdateUserRoleRequest request = new UpdateUserRoleRequest();
		request.setRole(UserRole.MENTOR);
		when(userRepository.findById(2)).thenReturn(Optional.of(targetAdmin));

		ForbiddenException exception = assertThrows(
				ForbiddenException.class,
				() -> userService.updateUserRole(2, request, 1));

		assertEquals(ErrorCode.ACCESS_DENIED, exception.getErrorCode());
		assertEquals("Không được thay đổi quyền của ADMIN khác", exception.getMessage());
	}

	private CreateUserRequest createUserRequest() {
		CreateUserRequest request = new CreateUserRequest();
		request.setUsername("mentor01");
		request.setPassword("123456");
		request.setFullName("Mentor One");
		request.setEmail("mentor@example.com");
		request.setPhoneNumber("0123456789");
		request.setRole(UserRole.MENTOR);
		return request;
	}
}
