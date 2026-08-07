package com.example.internship_management.service;

import com.example.internship_management.dto.request.CreateStudentRequest;
import com.example.internship_management.dto.request.UpdateStudentRequest;
import com.example.internship_management.dto.response.StudentResponse;
import java.util.List;

public interface StudentService {

	List<StudentResponse> getStudentsForCurrentUser();

	StudentResponse getStudentById(Integer studentId);

	StudentResponse createStudent(CreateStudentRequest request);

	StudentResponse updateStudent(Integer studentId, UpdateStudentRequest request);
}
