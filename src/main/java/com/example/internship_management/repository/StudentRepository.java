package com.example.internship_management.repository;

import com.example.internship_management.entity.Student;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Integer> {

	Optional<Student> findByStudentCode(String studentCode);

	boolean existsByStudentCode(String studentCode);

	boolean existsByStudentCodeAndStudentIdNot(String studentCode, Integer studentId);

	List<Student> findByStudentIdIn(Collection<Integer> studentIds);
}
