package com.example.internship_management.repository;

import com.example.internship_management.entity.InternshipPhase;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InternshipPhaseRepository extends JpaRepository<InternshipPhase, Integer> {

	boolean existsByPhaseName(String phaseName);

	Optional<InternshipPhase> findByPhaseName(String phaseName);
}
