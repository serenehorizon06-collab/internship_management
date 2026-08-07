package com.example.internship_management.security;

import com.example.internship_management.entity.User;
import com.example.internship_management.entity.UserRole;
import java.util.Collection;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class CustomUserDetails implements UserDetails {

	private final Integer userId;
	private final String username;
	private final String passwordHash;
	private final UserRole role;
	private final Boolean isActive;

	public CustomUserDetails(User user) {
		this.userId = user.getUserId();
		this.username = user.getUsername();
		this.passwordHash = user.getPasswordHash();
		this.role = user.getRole();
		this.isActive = user.getIsActive();
	}

	public Integer getUserId() {
		return userId;
	}

	public UserRole getRole() {
		return role;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
	}

	@Override
	public String getPassword() {
		return passwordHash;
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return Boolean.TRUE.equals(isActive);
	}
}
