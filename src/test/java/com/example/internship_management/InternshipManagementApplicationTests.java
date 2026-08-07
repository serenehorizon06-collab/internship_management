package com.example.internship_management;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.ActiveProfiles;
import com.example.internship_management.repository.InternshipAssignmentRepository;
import com.example.internship_management.repository.StudentRepository;
import com.example.internship_management.repository.UserRepository;

@SpringBootTest
@ActiveProfiles("test")
class InternshipManagementApplicationTests {

	@MockitoBean
	private UserRepository userRepository;

	@MockitoBean
	private StudentRepository studentRepository;

	@MockitoBean
	private InternshipAssignmentRepository internshipAssignmentRepository;

	@Test
	void contextLoads() {
	}

}
