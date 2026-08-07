package com.example.internship_management.service.impl;

import com.example.internship_management.dto.request.CreateInternshipPhaseRequest;
import com.example.internship_management.dto.request.UpdateInternshipPhaseRequest;
import com.example.internship_management.dto.response.InternshipPhaseResponse;
import com.example.internship_management.entity.InternshipPhase;
import com.example.internship_management.exception.DuplicateResourceException;
import com.example.internship_management.exception.InvalidInputException;
import com.example.internship_management.exception.ResourceNotFoundException;
import com.example.internship_management.repository.InternshipPhaseRepository;
import com.example.internship_management.service.InternshipPhaseService;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class InternshipPhaseServiceImpl implements InternshipPhaseService {

	private final InternshipPhaseRepository internshipPhaseRepository;

	@Override
	@Transactional(readOnly = true)
	public List<InternshipPhaseResponse> getAllPhases() {
		return internshipPhaseRepository.findAll()
				.stream()
				.map(InternshipPhaseResponse::from)
				.toList();
	}

	@Override
	@Transactional(readOnly = true)
	public InternshipPhaseResponse getPhaseById(Integer phaseId) {
		return InternshipPhaseResponse.from(findPhaseById(phaseId));
	}

	@Override
	@Transactional
	public InternshipPhaseResponse createPhase(CreateInternshipPhaseRequest request) {
		validateDateRange(request.getStartDate(), request.getEndDate());
		validateNewPhaseName(request.getPhaseName());

		InternshipPhase phase = new InternshipPhase();
		phase.setPhaseName(request.getPhaseName());
		phase.setStartDate(request.getStartDate());
		phase.setEndDate(request.getEndDate());
		phase.setDescription(request.getDescription());

		return InternshipPhaseResponse.from(internshipPhaseRepository.save(phase));
	}

	@Override
	@Transactional
	public InternshipPhaseResponse updatePhase(Integer phaseId, UpdateInternshipPhaseRequest request) {
		InternshipPhase phase = findPhaseById(phaseId);
		validateDateRange(request.getStartDate(), request.getEndDate());
		validatePhaseNameForUpdate(request.getPhaseName(), phaseId);

		phase.setPhaseName(request.getPhaseName());
		phase.setStartDate(request.getStartDate());
		phase.setEndDate(request.getEndDate());
		phase.setDescription(request.getDescription());

		return InternshipPhaseResponse.from(internshipPhaseRepository.save(phase));
	}

	@Override
	@Transactional
	public void deletePhase(Integer phaseId) {
		InternshipPhase phase = findPhaseById(phaseId);
		internshipPhaseRepository.delete(phase);
	}

	private InternshipPhase findPhaseById(Integer phaseId) {
		return internshipPhaseRepository.findById(phaseId)
				.orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy giai đoạn thực tập"));
	}

	private void validateNewPhaseName(String phaseName) {
		if (internshipPhaseRepository.existsByPhaseName(phaseName)) {
			throw new DuplicateResourceException("Tên giai đoạn thực tập đã tồn tại");
		}
	}

	private void validatePhaseNameForUpdate(String phaseName, Integer phaseId) {
		if (internshipPhaseRepository.existsByPhaseNameAndPhaseIdNot(phaseName, phaseId)) {
			throw new DuplicateResourceException("Tên giai đoạn thực tập đã tồn tại");
		}
	}

	private void validateDateRange(LocalDate startDate, LocalDate endDate) {
		if (startDate != null && endDate != null && startDate.isAfter(endDate)) {
			throw new InvalidInputException("Ngày bắt đầu không được sau ngày kết thúc");
		}
	}
}
