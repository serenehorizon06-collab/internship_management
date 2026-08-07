package com.example.internship_management.exception;

import org.springframework.http.HttpStatus;

public class UnauthorizedException extends AppException {

	public UnauthorizedException(ErrorCode errorCode, String message) {
		super(errorCode, HttpStatus.UNAUTHORIZED, message);
	}

	public UnauthorizedException(String message) {
		super(ErrorCode.INVALID_JWT_TOKEN, HttpStatus.UNAUTHORIZED, message);
	}
}
