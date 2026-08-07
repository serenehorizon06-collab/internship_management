package com.example.internship_management.exception;

import org.springframework.http.HttpStatus;

public class InvalidAssignmentStateException extends AppException {

	public InvalidAssignmentStateException(String message) {
		super(ErrorCode.INVALID_ASSIGNMENT_STATE, HttpStatus.CONFLICT, message);
	}
}
