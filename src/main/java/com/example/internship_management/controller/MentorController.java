package com.example.internship_management.controller;

import com.example.internship_management.common.ApiResponse;
import com.example.internship_management.dto.request.CreateMentorRequest;
import com.example.internship_management.dto.request.UpdateMentorRequest;
import com.example.internship_management.dto.response.MentorResponse;
import com.example.internship_management.service.MentorService;
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
@RequestMapping("/api/mentors")
@RequiredArgsConstructor
public class MentorController {

	private final MentorService mentorService;

	@GetMapping
	public ResponseEntity<ApiResponse<List<MentorResponse>>> getMentors() {
		return ResponseEntity.ok(ApiResponse.success(
				"Lấy danh sách giáo viên hướng dẫn thành công",
				mentorService.getAllMentors()));
	}

	@GetMapping("/{mentorId}")
	public ResponseEntity<ApiResponse<MentorResponse>> getMentorById(@PathVariable("mentorId") Integer mentorId) {
		return ResponseEntity.ok(ApiResponse.success(
				"Lấy thông tin giáo viên hướng dẫn thành công",
				mentorService.getMentorById(mentorId)));
	}

	@PostMapping
	public ResponseEntity<ApiResponse<MentorResponse>> createMentor(@Valid @RequestBody CreateMentorRequest request) {
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(ApiResponse.created(
						"Tạo thông tin giáo viên hướng dẫn thành công",
						mentorService.createMentor(request)));
	}

	@PutMapping("/{mentorId}")
	public ResponseEntity<ApiResponse<MentorResponse>> updateMentor(
			@PathVariable("mentorId") Integer mentorId,
			@Valid @RequestBody UpdateMentorRequest request) {
		return ResponseEntity.ok(ApiResponse.success(
				"Cập nhật thông tin giáo viên hướng dẫn thành công",
				mentorService.updateMentor(mentorId, request)));
	}
}
