package com.example.internship_management.repository;

import com.example.internship_management.entity.Mentor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MentorRepository extends JpaRepository<Mentor, Integer> {
}
