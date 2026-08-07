package com.example.internship_management.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.example.internship_management.dto.request.CreateMentorRequest;
import com.example.internship_management.dto.request.UpdateMentorRequest;
import com.example.internship_management.entity.Mentor;
import com.example.internship_management.entity.User;
import com.example.internship_management.entity.UserRole;
import com.example.internship_management.exception.DuplicateResourceException;
import com.example.internship_management.exception.ErrorCode;
import com.example.internship_management.exception.ForbiddenException;
import com.example.internship_management.exception.InvalidInputException;
import com.example.internship_management.repository.MentorRepository;
import com.example.internship_management.repository.UserRepository;
import com.example.internship_management.security.CustomUserDetails;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

class MentorServiceImplTests {

	private final MentorRepository mentorRepository = mock(MentorRepository.class);
	private final UserRepository userRepository = mock(UserRepository.class);
	private final MentorServiceImpl mentorService = new MentorServiceImpl(mentorRepository, userRepository);

	@AfterEach
	void clearSecurityContext() {
		SecurityContextHolder.clearContext();
	}

	@Test
	void createMentorRequiresExistingUserRoleMentor() {
		CreateMentorRequest request = createMentorRequest();
		when(userRepository.findById(1)).thenReturn(Optional.of(user(1, UserRole.STUDENT)));

		InvalidInputException exception = assertThrows(
				InvalidInputException.class,
				() -> mentorService.createMentor(request));

		assertEquals(ErrorCode.INVALID_INPUT_DATA, exception.getErrorCode());
	}

	@Test
	void createMentorFailsIfProfileAlreadyExists() {
		CreateMentorRequest request = createMentorRequest();
		when(userRepository.findById(1)).thenReturn(Optional.of(user(1, UserRole.MENTOR)));
		when(mentorRepository.existsById(1)).thenReturn(true);

		DuplicateResourceException exception = assertThrows(
				DuplicateResourceException.class,
				() -> mentorService.createMentor(request));

		assertEquals(ErrorCode.DUPLICATE_RESOURCE, exception.getErrorCode());
	}

	@Test
	void mentorCannotViewAnotherMentorProfile() {
		authenticateAs(user(1, UserRole.MENTOR));
		when(mentorRepository.findById(2)).thenReturn(Optional.of(mentor(2)));

		ForbiddenException exception = assertThrows(
				ForbiddenException.class,
				() -> mentorService.getMentorById(2));

		assertEquals(ErrorCode.ACCESS_DENIED, exception.getErrorCode());
	}

	@Test
	void mentorCannotUpdateAnotherMentorProfile() {
		authenticateAs(user(1, UserRole.MENTOR));
		when(mentorRepository.findById(2)).thenReturn(Optional.of(mentor(2)));
		UpdateMentorRequest request = new UpdateMentorRequest();
		request.setDepartment("Computer Science");

		ForbiddenException exception = assertThrows(
				ForbiddenException.class,
				() -> mentorService.updateMentor(2, request));

		assertEquals(ErrorCode.ACCESS_DENIED, exception.getErrorCode());
	}

	private void authenticateAs(User user) {
		CustomUserDetails principal = new CustomUserDetails(user);
		SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(
				principal,
				null,
				principal.getAuthorities()));
	}

	private CreateMentorRequest createMentorRequest() {
		CreateMentorRequest request = new CreateMentorRequest();
		request.setMentorId(1);
		request.setDepartment("Computer Science");
		request.setAcademicRank("ThS");
		return request;
	}

	private Mentor mentor(Integer mentorId) {
		Mentor mentor = new Mentor();
		mentor.setMentorId(mentorId);
		mentor.setDepartment("Computer Science");
		return mentor;
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
