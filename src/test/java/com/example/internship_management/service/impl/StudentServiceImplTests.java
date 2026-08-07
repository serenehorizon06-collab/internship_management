package com.example.internship_management.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.example.internship_management.dto.request.CreateStudentRequest;
import com.example.internship_management.entity.Student;
import com.example.internship_management.entity.User;
import com.example.internship_management.entity.UserRole;
import com.example.internship_management.exception.DuplicateResourceException;
import com.example.internship_management.exception.ErrorCode;
import com.example.internship_management.exception.ForbiddenException;
import com.example.internship_management.exception.InvalidInputException;
import com.example.internship_management.repository.InternshipAssignmentRepository;
import com.example.internship_management.repository.StudentRepository;
import com.example.internship_management.repository.UserRepository;
import com.example.internship_management.security.CustomUserDetails;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

class StudentServiceImplTests {

	private final StudentRepository studentRepository = mock(StudentRepository.class);
	private final UserRepository userRepository = mock(UserRepository.class);
	private final InternshipAssignmentRepository internshipAssignmentRepository = mock(InternshipAssignmentRepository.class);
	private final StudentServiceImpl studentService = new StudentServiceImpl(
			studentRepository,
			userRepository,
			internshipAssignmentRepository);

	@AfterEach
	void clearSecurityContext() {
		SecurityContextHolder.clearContext();
	}

	@Test
	void createStudentRequiresExistingUserRoleStudent() {
		CreateStudentRequest request = createStudentRequest();
		User mentorUser = user(1, UserRole.MENTOR);
		when(userRepository.findById(1)).thenReturn(Optional.of(mentorUser));

		InvalidInputException exception = assertThrows(
				InvalidInputException.class,
				() -> studentService.createStudent(request));

		assertEquals(ErrorCode.INVALID_INPUT_DATA, exception.getErrorCode());
	}

	@Test
	void createStudentWithDuplicateStudentCodeThrowsDuplicateResource() {
		CreateStudentRequest request = createStudentRequest();
		User studentUser = user(1, UserRole.STUDENT);
		when(userRepository.findById(1)).thenReturn(Optional.of(studentUser));
		when(studentRepository.existsById(1)).thenReturn(false);
		when(studentRepository.existsByStudentCode("SV001")).thenReturn(true);

		DuplicateResourceException exception = assertThrows(
				DuplicateResourceException.class,
				() -> studentService.createStudent(request));

		assertEquals(ErrorCode.DUPLICATE_RESOURCE, exception.getErrorCode());
	}

	@Test
	void studentCannotViewAnotherStudentProfile() {
		authenticateAs(user(1, UserRole.STUDENT));
		when(studentRepository.findById(2)).thenReturn(Optional.of(student(2)));

		ForbiddenException exception = assertThrows(
				ForbiddenException.class,
				() -> studentService.getStudentById(2));

		assertEquals(ErrorCode.ACCESS_DENIED, exception.getErrorCode());
	}

	@Test
	void mentorCannotViewUnassignedStudent() {
		authenticateAs(user(10, UserRole.MENTOR));
		when(studentRepository.findById(2)).thenReturn(Optional.of(student(2)));
		when(internshipAssignmentRepository.existsByMentorIdAndStudentId(10, 2)).thenReturn(false);

		ForbiddenException exception = assertThrows(
				ForbiddenException.class,
				() -> studentService.getStudentById(2));

		assertEquals(ErrorCode.ACCESS_DENIED, exception.getErrorCode());
	}

	private void authenticateAs(User user) {
		CustomUserDetails principal = new CustomUserDetails(user);
		SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(
				principal,
				null,
				principal.getAuthorities()));
	}

	private CreateStudentRequest createStudentRequest() {
		CreateStudentRequest request = new CreateStudentRequest();
		request.setStudentId(1);
		request.setStudentCode("SV001");
		request.setMajor("Software Engineering");
		request.setClassName("SE01");
		return request;
	}

	private Student student(Integer studentId) {
		Student student = new Student();
		student.setStudentId(studentId);
		student.setStudentCode("SV00" + studentId);
		return student;
	}

	private User user(Integer userId, UserRole role) {
		User user = new User();
		user.setUserId(userId);
		user.setUsername("user" + userId);
		user.setPasswordHash("$2a$10$hashed");
		user.setFullName("User " + userId);
		user.setEmail("user" + userId + "@example.com");
		user.setRole(role);
		user.setIsActive(true);
		return user;
	}
}
