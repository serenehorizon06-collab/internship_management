package com.example.internship_management.service.impl;

import com.example.internship_management.dto.request.CreateMentorRequest;
import com.example.internship_management.dto.request.UpdateMentorRequest;
import com.example.internship_management.dto.response.MentorResponse;
import com.example.internship_management.dto.response.UserSummaryResponse;
import com.example.internship_management.entity.Mentor;
import com.example.internship_management.entity.User;
import com.example.internship_management.entity.UserRole;
import com.example.internship_management.exception.DuplicateResourceException;
import com.example.internship_management.exception.ForbiddenException;
import com.example.internship_management.exception.InvalidInputException;
import com.example.internship_management.exception.ResourceNotFoundException;
import com.example.internship_management.exception.UnauthorizedException;
import com.example.internship_management.repository.MentorRepository;
import com.example.internship_management.repository.UserRepository;
import com.example.internship_management.security.CustomUserDetails;
import com.example.internship_management.service.MentorService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MentorServiceImpl implements MentorService {

	private final MentorRepository mentorRepository;
	private final UserRepository userRepository;

	@Override
	@Transactional(readOnly = true)
	public List<MentorResponse> getAllMentors() {
		CustomUserDetails currentUser = getCurrentUser();
		if (!UserRole.ADMIN.equals(currentUser.getRole()) && !UserRole.STUDENT.equals(currentUser.getRole())) {
			throw new ForbiddenException("Không có quyền thực hiện thao tác này");
		}

		return mentorRepository.findAll()
				.stream()
				.map(this::toMentorResponse)
				.toList();
	}

	@Override
	@Transactional(readOnly = true)
	public MentorResponse getMentorById(Integer mentorId) {
		Mentor mentor = findMentorById(mentorId);
		ensureCanViewMentor(mentorId);
		return toMentorResponse(mentor);
	}

	@Override
	@Transactional
	public MentorResponse createMentor(CreateMentorRequest request) {
		User user = userRepository.findById(request.getMentorId())
				.orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng"));
		if (!UserRole.MENTOR.equals(user.getRole())) {
			throw new InvalidInputException("Người dùng liên kết phải có vai trò MENTOR");
		}
		if (mentorRepository.existsById(request.getMentorId())) {
			throw new DuplicateResourceException("Thông tin giáo viên hướng dẫn đã tồn tại");
		}

		Mentor mentor = new Mentor();
		mentor.setMentorId(request.getMentorId());
		mentor.setDepartment(request.getDepartment());
		mentor.setAcademicRank(request.getAcademicRank());

		return toMentorResponse(mentorRepository.save(mentor));
	}

	@Override
	@Transactional
	public MentorResponse updateMentor(Integer mentorId, UpdateMentorRequest request) {
		Mentor mentor = findMentorById(mentorId);
		ensureCanUpdateMentor(mentorId);

		mentor.setDepartment(request.getDepartment());
		mentor.setAcademicRank(request.getAcademicRank());

		return toMentorResponse(mentorRepository.save(mentor));
	}

	private void ensureCanViewMentor(Integer mentorId) {
		CustomUserDetails currentUser = getCurrentUser();
		if (UserRole.ADMIN.equals(currentUser.getRole()) || UserRole.STUDENT.equals(currentUser.getRole())) {
			return;
		}
		if (UserRole.MENTOR.equals(currentUser.getRole()) && currentUser.getUserId().equals(mentorId)) {
			return;
		}
		throw new ForbiddenException("Không có quyền thực hiện thao tác này");
	}

	private void ensureCanUpdateMentor(Integer mentorId) {
		CustomUserDetails currentUser = getCurrentUser();
		if (UserRole.ADMIN.equals(currentUser.getRole())) {
			return;
		}
		if (UserRole.MENTOR.equals(currentUser.getRole()) && currentUser.getUserId().equals(mentorId)) {
			return;
		}
		throw new ForbiddenException("Không có quyền thực hiện thao tác này");
	}

	private Mentor findMentorById(Integer mentorId) {
		return mentorRepository.findById(mentorId)
				.orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy giáo viên hướng dẫn"));
	}

	private MentorResponse toMentorResponse(Mentor mentor) {
		User user = userRepository.findById(mentor.getMentorId())
				.orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng"));
		return MentorResponse.from(mentor, UserSummaryResponse.from(user));
	}

	private CustomUserDetails getCurrentUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails userDetails) {
			return userDetails;
		}
		throw new UnauthorizedException("Token xác thực không hợp lệ hoặc bị thiếu");
	}
}
