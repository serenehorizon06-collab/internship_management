package com.example.internship_management.common;

import com.example.internship_management.exception.ErrorCode;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;
import java.util.List;

public class ErrorResponse {

	private final boolean success;

	@JsonProperty("status_code")
	private final int statusCode;

	@JsonProperty("error_code")
	private final ErrorCode errorCode;

	private final String message;
	private final List<FieldErrorResponse> errors;
	private final OffsetDateTime timestamp;

	public ErrorResponse(int statusCode, ErrorCode errorCode, String message, List<FieldErrorResponse> errors) {
		this.success = false;
		this.statusCode = statusCode;
		this.errorCode = errorCode;
		this.message = message;
		this.errors = errors == null ? List.of() : List.copyOf(errors);
		this.timestamp = OffsetDateTime.now();
	}

	public boolean isSuccess() {
		return success;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public ErrorCode getErrorCode() {
		return errorCode;
	}

	public String getMessage() {
		return message;
	}

	public List<FieldErrorResponse> getErrors() {
		return errors;
	}

	public OffsetDateTime getTimestamp() {
		return timestamp;
	}
}
