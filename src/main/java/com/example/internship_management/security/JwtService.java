package com.example.internship_management.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.internship_management.entity.User;
import com.example.internship_management.exception.ErrorCode;
import com.example.internship_management.exception.UnauthorizedException;
import java.time.Instant;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

	private final String secret;
	private final Long expirationMs;

	public JwtService(
			@Value("${app.jwt.secret}") String secret,
			@Value("${app.jwt.expiration-ms}") Long expirationMs) {
		this.secret = secret;
		this.expirationMs = expirationMs;
	}

	public String generateToken(User user) {
		Instant now = Instant.now();
		return JWT.create()
				.withSubject(user.getUsername())
				.withClaim("userId", user.getUserId())
				.withClaim("role", user.getRole().name())
				.withIssuedAt(now)
				.withExpiresAt(now.plusMillis(expirationMs))
				.sign(algorithm());
	}

	public String extractUsername(String token) {
		return verifyToken(token).getSubject();
	}

	public Integer extractUserId(String token) {
		return verifyToken(token).getClaim("userId").asInt();
	}

	public String extractRole(String token) {
		return verifyToken(token).getClaim("role").asString();
	}

	public boolean isTokenValid(String token, UserDetails userDetails) {
		String username = extractUsername(token);
		return username.equals(userDetails.getUsername());
	}

	public Long getExpirationMs() {
		return expirationMs;
	}

	private DecodedJWT verifyToken(String token) {
		try {
			JWTVerifier verifier = JWT.require(algorithm()).build();
			return verifier.verify(token);
		} catch (TokenExpiredException exception) {
			throw new UnauthorizedException(ErrorCode.EXPIRED_JWT_TOKEN, "Token xác thực đã hết hạn");
		} catch (JWTVerificationException exception) {
			throw new UnauthorizedException(ErrorCode.INVALID_JWT_TOKEN, "Token xác thực không hợp lệ hoặc bị thiếu");
		}
	}

	private Algorithm algorithm() {
		return Algorithm.HMAC256(secret);
	}
}
