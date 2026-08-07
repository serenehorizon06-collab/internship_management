package com.example.internship_management.repository;

import com.example.internship_management.entity.Student;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Integer> {

	Optional<Student> findByStudentCode(String studentCode);

	boolean existsByStudentCode(String studentCode);
}
