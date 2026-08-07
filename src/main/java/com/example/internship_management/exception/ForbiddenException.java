package com.example.internship_management.exception;

import org.springframework.http.HttpStatus;

public class ForbiddenException extends AppException {

	public ForbiddenException(String message) {
		super(ErrorCode.ACCESS_DENIED, HttpStatus.FORBIDDEN, message);
	}
}
