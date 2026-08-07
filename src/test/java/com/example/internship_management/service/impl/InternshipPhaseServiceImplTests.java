package com.example.internship_management.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.example.internship_management.dto.request.CreateInternshipPhaseRequest;
import com.example.internship_management.dto.request.UpdateInternshipPhaseRequest;
import com.example.internship_management.entity.InternshipPhase;
import com.example.internship_management.exception.DuplicateResourceException;
import com.example.internship_management.exception.ErrorCode;
import com.example.internship_management.exception.InvalidInputException;
import com.example.internship_management.exception.ResourceNotFoundException;
import com.example.internship_management.repository.InternshipPhaseRepository;
import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class InternshipPhaseServiceImplTests {

	private final InternshipPhaseRepository internshipPhaseRepository = mock(InternshipPhaseRepository.class);
	private final InternshipPhaseServiceImpl internshipPhaseService =
			new InternshipPhaseServiceImpl(internshipPhaseRepository);

	@Test
	void createPhaseRejectsStartDateAfterEndDate() {
		CreateInternshipPhaseRequest request = createRequest();
		request.setStartDate(LocalDate.of(2026, 12, 1));
		request.setEndDate(LocalDate.of(2026, 8, 1));

		InvalidInputException exception = assertThrows(
				InvalidInputException.class,
				() -> internshipPhaseService.createPhase(request));

		assertEquals(ErrorCode.INVALID_INPUT_DATA, exception.getErrorCode());
		assertEquals("Ngày bắt đầu không được sau ngày kết thúc", exception.getMessage());
	}

	@Test
	void createPhaseRejectsDuplicatePhaseName() {
		CreateInternshipPhaseRequest request = createRequest();
		when(internshipPhaseRepository.existsByPhaseName("Thực tập cơ sở 1")).thenReturn(true);

		DuplicateResourceException exception = assertThrows(
				DuplicateResourceException.class,
				() -> internshipPhaseService.createPhase(request));

		assertEquals(ErrorCode.DUPLICATE_RESOURCE, exception.getErrorCode());
	}

	@Test
	void updatePhaseRejectsDuplicatePhaseNameExcludingCurrentPhase() {
		UpdateInternshipPhaseRequest request = updateRequest();
		when(internshipPhaseRepository.findById(1)).thenReturn(Optional.of(phase(1)));
		when(internshipPhaseRepository.existsByPhaseNameAndPhaseIdNot("Thực tập tốt nghiệp", 1)).thenReturn(true);

		DuplicateResourceException exception = assertThrows(
				DuplicateResourceException.class,
				() -> internshipPhaseService.updatePhase(1, request));

		assertEquals(ErrorCode.DUPLICATE_RESOURCE, exception.getErrorCode());
	}

	@Test
	void getPhaseByMissingIdThrowsResourceNotFound() {
		when(internshipPhaseRepository.findById(99)).thenReturn(Optional.empty());

		ResourceNotFoundException exception = assertThrows(
				ResourceNotFoundException.class,
				() -> internshipPhaseService.getPhaseById(99));

		assertEquals(ErrorCode.RESOURCE_NOT_FOUND, exception.getErrorCode());
	}

	private CreateInternshipPhaseRequest createRequest() {
		CreateInternshipPhaseRequest request = new CreateInternshipPhaseRequest();
		request.setPhaseName("Thực tập cơ sở 1");
		request.setStartDate(LocalDate.of(2026, 8, 1));
		request.setEndDate(LocalDate.of(2026, 12, 1));
		request.setDescription("Giai đoạn thực tập cơ sở");
		return request;
	}

	private UpdateInternshipPhaseRequest updateRequest() {
		UpdateInternshipPhaseRequest request = new UpdateInternshipPhaseRequest();
		request.setPhaseName("Thực tập tốt nghiệp");
		request.setStartDate(LocalDate.of(2026, 8, 1));
		request.setEndDate(LocalDate.of(2026, 12, 1));
		request.setDescription("Giai đoạn thực tập tốt nghiệp");
		return request;
	}

	private InternshipPhase phase(Integer phaseId) {
		InternshipPhase phase = new InternshipPhase();
		phase.setPhaseId(phaseId);
		phase.setPhaseName("Thực tập cơ sở 1");
		phase.setStartDate(LocalDate.of(2026, 8, 1));
		phase.setEndDate(LocalDate.of(2026, 12, 1));
		return phase;
	}
}
