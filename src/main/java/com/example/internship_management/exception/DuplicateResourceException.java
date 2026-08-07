package com.example.internship_management.exception;

import org.springframework.http.HttpStatus;

public class DuplicateResourceException extends AppException {

	public DuplicateResourceException(String message) {
		super(ErrorCode.DUPLICATE_RESOURCE, HttpStatus.BAD_REQUEST, message);
	}
}
