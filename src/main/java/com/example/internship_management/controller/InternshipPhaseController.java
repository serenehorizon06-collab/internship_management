package com.example.internship_management.controller;

import com.example.internship_management.common.ApiResponse;
import com.example.internship_management.dto.request.CreateInternshipPhaseRequest;
import com.example.internship_management.dto.request.UpdateInternshipPhaseRequest;
import com.example.internship_management.dto.response.InternshipPhaseResponse;
import com.example.internship_management.service.InternshipPhaseService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/internship_phases")
@RequiredArgsConstructor
public class InternshipPhaseController {

	private final InternshipPhaseService internshipPhaseService;

	@GetMapping
	public ResponseEntity<ApiResponse<List<InternshipPhaseResponse>>> getPhases() {
		return ResponseEntity.ok(ApiResponse.success(
				"Lấy danh sách giai đoạn thực tập thành công",
				internshipPhaseService.getAllPhases()));
	}

	@GetMapping("/{phaseId}")
	public ResponseEntity<ApiResponse<InternshipPhaseResponse>> getPhaseById(
			@PathVariable("phaseId") Integer phaseId) {
		return ResponseEntity.ok(ApiResponse.success(
				"Lấy thông tin giai đoạn thực tập thành công",
				internshipPhaseService.getPhaseById(phaseId)));
	}

	@PostMapping
	public ResponseEntity<ApiResponse<InternshipPhaseResponse>> createPhase(
			@Valid @RequestBody CreateInternshipPhaseRequest request) {
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(ApiResponse.created(
						"Tạo giai đoạn thực tập thành công",
						internshipPhaseService.createPhase(request)));
	}

	@PutMapping("/{phaseId}")
	public ResponseEntity<ApiResponse<InternshipPhaseResponse>> updatePhase(
			@PathVariable("phaseId") Integer phaseId,
			@Valid @RequestBody UpdateInternshipPhaseRequest request) {
		return ResponseEntity.ok(ApiResponse.success(
				"Cập nhật giai đoạn thực tập thành công",
				internshipPhaseService.updatePhase(phaseId, request)));
	}

	@DeleteMapping("/{phaseId}")
	public ResponseEntity<ApiResponse<Void>> deletePhase(@PathVariable("phaseId") Integer phaseId) {
		internshipPhaseService.deletePhase(phaseId);
		return ResponseEntity.ok(ApiResponse.<Void>success("Xóa giai đoạn thực tập thành công", null));
	}
}
