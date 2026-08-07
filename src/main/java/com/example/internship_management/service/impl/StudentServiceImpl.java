package com.example.internship_management.service.impl;

import com.example.internship_management.dto.request.CreateStudentRequest;
import com.example.internship_management.dto.request.UpdateStudentRequest;
import com.example.internship_management.dto.response.StudentResponse;
import com.example.internship_management.dto.response.UserSummaryResponse;
import com.example.internship_management.entity.InternshipAssignment;
import com.example.internship_management.entity.Student;
import com.example.internship_management.entity.User;
import com.example.internship_management.entity.UserRole;
import com.example.internship_management.exception.DuplicateResourceException;
import com.example.internship_management.exception.ForbiddenException;
import com.example.internship_management.exception.InvalidInputException;
import com.example.internship_management.exception.ResourceNotFoundException;
import com.example.internship_management.exception.UnauthorizedException;
import com.example.internship_management.repository.InternshipAssignmentRepository;
import com.example.internship_management.repository.StudentRepository;
import com.example.internship_management.repository.UserRepository;
import com.example.internship_management.security.CustomUserDetails;
import com.example.internship_management.service.StudentService;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

	private final StudentRepository studentRepository;
	private final UserRepository userRepository;
	private final InternshipAssignmentRepository internshipAssignmentRepository;

	@Override
	@Transactional(readOnly = true)
	public List<StudentResponse> getStudentsForCurrentUser() {
		CustomUserDetails currentUser = getCurrentUser();
		if (UserRole.ADMIN.equals(currentUser.getRole())) {
			return studentRepository.findAll()
					.stream()
					.map(this::toStudentResponse)
					.toList();
		}

		if (UserRole.MENTOR.equals(currentUser.getRole())) {
			Set<Integer> studentIds = internshipAssignmentRepository.findByMentorId(currentUser.getUserId())
					.stream()
					.map(InternshipAssignment::getStudentId)
					.collect(Collectors.toSet());
			if (studentIds.isEmpty()) {
				return List.of();
			}
			return studentRepository.findByStudentIdIn(studentIds)
					.stream()
					.map(this::toStudentResponse)
					.toList();
		}

		throw new ForbiddenException("Không có quyền thực hiện thao tác này");
	}

	@Override
	@Transactional(readOnly = true)
	public StudentResponse getStudentById(Integer studentId) {
		Student student = findStudentById(studentId);
		ensureCanViewStudent(studentId);
		return toStudentResponse(student);
	}

	@Override
	@Transactional
	public StudentResponse createStudent(CreateStudentRequest request) {
		User user = userRepository.findById(request.getStudentId())
				.orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng"));
		if (!UserRole.STUDENT.equals(user.getRole())) {
			throw new InvalidInputException("Người dùng liên kết phải có vai trò STUDENT");
		}
		if (studentRepository.existsById(request.getStudentId())) {
			throw new DuplicateResourceException("Thông tin sinh viên đã tồn tại");
		}
		validateNewStudentCode(request.getStudentCode());

		Student student = new Student();
		student.setStudentId(request.getStudentId());
		student.setStudentCode(request.getStudentCode());
		student.setMajor(request.getMajor());
		student.setClassName(request.getClassName());
		student.setDateOfBirth(request.getDateOfBirth());
		student.setAddress(request.getAddress());

		return toStudentResponse(studentRepository.save(student));
	}

	@Override
	@Transactional
	public StudentResponse updateStudent(Integer studentId, UpdateStudentRequest request) {
		Student student = findStudentById(studentId);
		ensureCanUpdateStudent(studentId);
		validateStudentCodeForUpdate(request.getStudentCode(), studentId);

		student.setStudentCode(request.getStudentCode());
		student.setMajor(request.getMajor());
		student.setClassName(request.getClassName());
		student.setDateOfBirth(request.getDateOfBirth());
		student.setAddress(request.getAddress());

		return toStudentResponse(studentRepository.save(student));
	}

	private void ensureCanViewStudent(Integer studentId) {
		CustomUserDetails currentUser = getCurrentUser();
		if (UserRole.ADMIN.equals(currentUser.getRole())) {
			return;
		}
		if (UserRole.STUDENT.equals(currentUser.getRole()) && currentUser.getUserId().equals(studentId)) {
			return;
		}
		if (UserRole.MENTOR.equals(currentUser.getRole())
				&& internshipAssignmentRepository.existsByMentorIdAndStudentId(currentUser.getUserId(), studentId)) {
			return;
		}
		throw new ForbiddenException("Không có quyền thực hiện thao tác này");
	}

	private void ensureCanUpdateStudent(Integer studentId) {
		CustomUserDetails currentUser = getCurrentUser();
		if (UserRole.ADMIN.equals(currentUser.getRole())) {
			return;
		}
		if (UserRole.STUDENT.equals(currentUser.getRole()) && currentUser.getUserId().equals(studentId)) {
			return;
		}
		throw new ForbiddenException("Không có quyền thực hiện thao tác này");
	}

	private Student findStudentById(Integer studentId) {
		return studentRepository.findById(studentId)
				.orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sinh viên"));
	}

	private StudentResponse toStudentResponse(Student student) {
		User user = userRepository.findById(student.getStudentId())
				.orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng"));
		return StudentResponse.from(student, UserSummaryResponse.from(user));
	}

	private void validateNewStudentCode(String studentCode) {
		if (studentRepository.existsByStudentCode(studentCode)) {
			throw new DuplicateResourceException("Mã sinh viên đã tồn tại");
		}
	}

	private void validateStudentCodeForUpdate(String studentCode, Integer studentId) {
		if (studentRepository.existsByStudentCodeAndStudentIdNot(studentCode, studentId)) {
			throw new DuplicateResourceException("Mã sinh viên đã tồn tại");
		}
	}

	private CustomUserDetails getCurrentUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails userDetails) {
			return userDetails;
		}
		throw new UnauthorizedException("Token xác thực không hợp lệ hoặc bị thiếu");
	}
}
