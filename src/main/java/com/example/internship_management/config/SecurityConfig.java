package com.example.internship_management.config;

import com.example.internship_management.security.CustomUserDetailsService;
import com.example.internship_management.security.JwtAuthenticationFilter;
import com.example.internship_management.security.RestAccessDeniedHandler;
import com.example.internship_management.security.RestAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	private final JwtAuthenticationFilter jwtAuthenticationFilter;
	private final CustomUserDetailsService customUserDetailsService;
	private final RestAuthenticationEntryPoint restAuthenticationEntryPoint;
	private final RestAccessDeniedHandler restAccessDeniedHandler;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		return http
				.csrf(AbstractHttpConfigurer::disable)
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.exceptionHandling(exception -> exception
						.authenticationEntryPoint(restAuthenticationEntryPoint)
						.accessDeniedHandler(restAccessDeniedHandler))
				.authorizeHttpRequests(authorize -> authorize
						.requestMatchers(HttpMethod.POST, "/api/auth/login").permitAll()
						.requestMatchers("/api/users", "/api/users/**").hasRole("ADMIN")
						.requestMatchers(HttpMethod.GET, "/api/students").hasAnyRole("ADMIN", "MENTOR")
						.requestMatchers(HttpMethod.GET, "/api/students/*").hasAnyRole("ADMIN", "MENTOR", "STUDENT")
						.requestMatchers(HttpMethod.POST, "/api/students").hasRole("ADMIN")
						.requestMatchers(HttpMethod.PUT, "/api/students/*").hasAnyRole("ADMIN", "STUDENT")
						.requestMatchers(HttpMethod.GET, "/api/mentors").hasAnyRole("ADMIN", "STUDENT")
						.requestMatchers(HttpMethod.GET, "/api/mentors/*").hasAnyRole("ADMIN", "MENTOR", "STUDENT")
						.requestMatchers(HttpMethod.POST, "/api/mentors").hasRole("ADMIN")
						.requestMatchers(HttpMethod.PUT, "/api/mentors/*").hasAnyRole("ADMIN", "MENTOR")
						.requestMatchers("/api/auth/me").authenticated()
						.requestMatchers("/api/**").authenticated()
						.anyRequest().permitAll())
				.authenticationProvider(authenticationProvider())
				.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
				.build();
	}

	@Bean
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider(customUserDetailsService);
		authenticationProvider.setPasswordEncoder(passwordEncoder());
		return authenticationProvider;
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) {
		return configuration.getAuthenticationManager();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
