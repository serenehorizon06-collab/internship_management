package com.example.internship_management.exception;

import com.example.internship_management.common.FieldErrorResponse;
import java.util.List;
import org.springframework.http.HttpStatus;

public class AppException extends RuntimeException {

	private final ErrorCode errorCode;
	private final HttpStatus httpStatus;
	private final List<FieldErrorResponse> errors;

	public AppException(ErrorCode errorCode, HttpStatus httpStatus, String message) {
		this(errorCode, httpStatus, message, List.of());
	}

	public AppException(ErrorCode errorCode, HttpStatus httpStatus, String message, List<FieldErrorResponse> errors) {
		super(message);
		this.errorCode = errorCode;
		this.httpStatus = httpStatus;
		this.errors = errors == null ? List.of() : List.copyOf(errors);
	}

	public ErrorCode getErrorCode() {
		return errorCode;
	}

	public HttpStatus getHttpStatus() {
		return httpStatus;
	}

	public List<FieldErrorResponse> getErrors() {
		return errors;
	}
}
