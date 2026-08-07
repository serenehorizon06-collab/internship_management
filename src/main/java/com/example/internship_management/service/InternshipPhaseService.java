package com.example.internship_management.service;

import com.example.internship_management.dto.request.CreateInternshipPhaseRequest;
import com.example.internship_management.dto.request.UpdateInternshipPhaseRequest;
import com.example.internship_management.dto.response.InternshipPhaseResponse;
import java.util.List;

public interface InternshipPhaseService {

	List<InternshipPhaseResponse> getAllPhases();

	InternshipPhaseResponse getPhaseById(Integer phaseId);

	InternshipPhaseResponse createPhase(CreateInternshipPhaseRequest request);

	InternshipPhaseResponse updatePhase(Integer phaseId, UpdateInternshipPhaseRequest request);

	void deletePhase(Integer phaseId);
}
