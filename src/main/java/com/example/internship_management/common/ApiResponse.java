package com.example.internship_management.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;
import org.springframework.http.HttpStatus;

public class ApiResponse<T> {

	private final boolean success;

	@JsonProperty("status_code")
	private final int statusCode;

	private final String message;
	private final T data;
	private final OffsetDateTime timestamp;

	private ApiResponse(HttpStatus status, String message, T data) {
		this.success = true;
		this.statusCode = status.value();
		this.message = message;
		this.data = data;
		this.timestamp = OffsetDateTime.now();
	}

	public static <T> ApiResponse<T> success(String message, T data) {
		return success(HttpStatus.OK, message, data);
	}

	public static <T> ApiResponse<T> created(String message, T data) {
		return success(HttpStatus.CREATED, message, data);
	}

	public static <T> ApiResponse<T> success(HttpStatus status, String message, T data) {
		return new ApiResponse<>(status, message, data);
	}

	public boolean isSuccess() {
		return success;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public String getMessage() {
		return message;
	}

	public T getData() {
		return data;
	}

	public OffsetDateTime getTimestamp() {
		return timestamp;
	}
}
