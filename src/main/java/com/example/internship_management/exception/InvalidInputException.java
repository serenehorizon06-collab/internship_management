package com.example.internship_management.exception;

import com.example.internship_management.common.FieldErrorResponse;
import java.util.List;
import org.springframework.http.HttpStatus;

public class InvalidInputException extends AppException {

	public InvalidInputException(String message) {
		super(ErrorCode.INVALID_INPUT_DATA, HttpStatus.BAD_REQUEST, message);
	}

	public InvalidInputException(String message, List<FieldErrorResponse> errors) {
		super(ErrorCode.INVALID_INPUT_DATA, HttpStatus.BAD_REQUEST, message, errors);
	}
}
