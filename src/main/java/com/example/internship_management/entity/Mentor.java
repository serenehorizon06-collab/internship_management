package com.example.internship_management.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "mentors")
public class Mentor {

	@Id
	@Column(name = "mentor_id")
	private Integer mentorId;

	@Column(name = "department", length = 100)
	private String department;

	@Column(name = "academic_rank", length = 50)
	private String academicRank;

	@Column(name = "created_at", nullable = false, insertable = false, updatable = false)
	private LocalDateTime createdAt;

	@Column(name = "updated_at", nullable = false, insertable = false, updatable = false)
	private LocalDateTime updatedAt;
}
