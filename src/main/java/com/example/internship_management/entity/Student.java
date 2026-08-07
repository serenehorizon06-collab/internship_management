package com.example.internship_management.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "students")
public class Student {

	@Id
	@Column(name = "student_id")
	private Integer studentId;

	@Column(name = "student_code", nullable = false, unique = true, length = 20)
	private String studentCode;

	@Column(name = "major", length = 100)
	private String major;

	@Column(name = "class_name", length = 50)
	private String className;

	@Column(name = "date_of_birth")
	private LocalDate dateOfBirth;

	@Column(name = "address")
	private String address;

	@Column(name = "created_at", nullable = false, insertable = false, updatable = false)
	private LocalDateTime createdAt;

	@Column(name = "updated_at", nullable = false, insertable = false, updatable = false)
	private LocalDateTime updatedAt;
}
