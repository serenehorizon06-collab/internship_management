package com.example.internship_management.repository;

import com.example.internship_management.entity.EvaluationCriterion;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EvaluationCriterionRepository extends JpaRepository<EvaluationCriterion, Integer> {

	boolean existsByCriterionName(String criterionName);

	Optional<EvaluationCriterion> findByCriterionName(String criterionName);

	boolean existsByCriterionNameAndCriterionIdNot(String criterionName, Integer criterionId);
}
