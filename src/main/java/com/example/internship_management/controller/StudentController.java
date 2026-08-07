package com.example.internship_management.controller;

import com.example.internship_management.common.ApiResponse;
import com.example.internship_management.dto.request.CreateStudentRequest;
import com.example.internship_management.dto.request.UpdateStudentRequest;
import com.example.internship_management.dto.response.StudentResponse;
import com.example.internship_management.service.StudentService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
public class StudentController {

	private final StudentService studentService;

	@GetMapping
	public ResponseEntity<ApiResponse<List<StudentResponse>>> getStudents() {
		return ResponseEntity.ok(ApiResponse.success(
				"Lấy danh sách sinh viên thành công",
				studentService.getStudentsForCurrentUser()));
	}

	@GetMapping("/{studentId}")
	public ResponseEntity<ApiResponse<StudentResponse>> getStudentById(@PathVariable("studentId") Integer studentId) {
		return ResponseEntity.ok(ApiResponse.success(
				"Lấy thông tin sinh viên thành công",
				studentService.getStudentById(studentId)));
	}

	@PostMapping
	public ResponseEntity<ApiResponse<StudentResponse>> createStudent(@Valid @RequestBody CreateStudentRequest request) {
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(ApiResponse.created(
						"Tạo thông tin sinh viên thành công",
						studentService.createStudent(request)));
	}

	@PutMapping("/{studentId}")
	public ResponseEntity<ApiResponse<StudentResponse>> updateStudent(
			@PathVariable("studentId") Integer studentId,
			@Valid @RequestBody UpdateStudentRequest request) {
		return ResponseEntity.ok(ApiResponse.success(
				"Cập nhật thông tin sinh viên thành công",
				studentService.updateStudent(studentId, request)));
	}
}
