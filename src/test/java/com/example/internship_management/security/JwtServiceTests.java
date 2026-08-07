package com.example.internship_management.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.internship_management.entity.User;
import com.example.internship_management.entity.UserRole;
import org.junit.jupiter.api.Test;

class JwtServiceTests {

	private final JwtService jwtService = new JwtService(
			"CHANGE_ME_TO_A_LONG_SECRET_FOR_LOCAL_DEV_ONLY",
			86400000L);

	@Test
	void generateTokenContainsRequiredIdentityClaims() {
		User user = activeUser();

		String token = jwtService.generateToken(user);

		assertEquals("admin", jwtService.extractUsername(token));
		assertEquals(1, jwtService.extractUserId(token));
		assertEquals("ADMIN", jwtService.extractRole(token));
		assertTrue(jwtService.isTokenValid(token, new CustomUserDetails(user)));
	}

	private User activeUser() {
		User user = new User();
		user.setUserId(1);
		user.setUsername("admin");
		user.setPasswordHash("$2a$10$hashed");
		user.setFullName("System Admin");
		user.setEmail("admin@example.com");
		user.setRole(UserRole.ADMIN);
		user.setIsActive(true);
		return user;
	}
}
