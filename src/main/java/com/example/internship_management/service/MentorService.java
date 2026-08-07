package com.example.internship_management.service;

import com.example.internship_management.dto.request.CreateMentorRequest;
import com.example.internship_management.dto.request.UpdateMentorRequest;
import com.example.internship_management.dto.response.MentorResponse;
import java.util.List;

public interface MentorService {

	List<MentorResponse> getAllMentors();

	MentorResponse getMentorById(Integer mentorId);

	MentorResponse createMentor(CreateMentorRequest request);

	MentorResponse updateMentor(Integer mentorId, UpdateMentorRequest request);
}
