package com.example.internship_management.repository;

import com.example.internship_management.entity.InternshipAssignment;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InternshipAssignmentRepository extends JpaRepository<InternshipAssignment, Integer> {

	List<InternshipAssignment> findByMentorId(Integer mentorId);

	List<InternshipAssignment> findByStudentId(Integer studentId);

	boolean existsByStudentIdAndPhaseId(Integer studentId, Integer phaseId);
}
