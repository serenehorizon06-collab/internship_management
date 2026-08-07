package com.example.internship_management.repository;

import com.example.internship_management.entity.User;
import com.example.internship_management.entity.UserRole;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {

	Optional<User> findByUsername(String username);

	Optional<User> findByEmail(String email);

	boolean existsByUsername(String username);

	boolean existsByEmail(String email);

	List<User> findByRole(UserRole role);
}
