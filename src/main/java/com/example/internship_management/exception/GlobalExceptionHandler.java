package com.example.internship_management.exception;

import com.example.internship_management.common.ErrorResponse;
import com.example.internship_management.common.FieldErrorResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.util.List;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

	private static final String INVALID_INPUT_MESSAGE = "Dữ liệu đầu vào không hợp lệ";

	@ExceptionHandler(AppException.class)
	public ResponseEntity<ErrorResponse> handleAppException(AppException exception) {
		ErrorResponse response = new ErrorResponse(
				exception.getHttpStatus().value(),
				exception.getErrorCode(),
				exception.getMessage(),
				exception.getErrors());
		return ResponseEntity.status(exception.getHttpStatus()).body(response);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException exception) {
		List<FieldErrorResponse> errors = exception.getBindingResult()
				.getFieldErrors()
				.stream()
				.map(error -> new FieldErrorResponse(error.getField(), error.getDefaultMessage()))
				.toList();

		ErrorResponse response = new ErrorResponse(
				HttpStatus.BAD_REQUEST.value(),
				ErrorCode.INVALID_INPUT_DATA,
				INVALID_INPUT_MESSAGE,
				errors);
		return ResponseEntity.badRequest().body(response);
	}

	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<ErrorResponse> handleConstraintViolation(ConstraintViolationException exception) {
		List<FieldErrorResponse> errors = exception.getConstraintViolations()
				.stream()
				.map(this::toFieldError)
				.toList();

		ErrorResponse response = new ErrorResponse(
				HttpStatus.BAD_REQUEST.value(),
				ErrorCode.INVALID_INPUT_DATA,
				INVALID_INPUT_MESSAGE,
				errors);
		return ResponseEntity.badRequest().body(response);
	}

	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<ErrorResponse> handleDataIntegrityViolation() {
		ErrorResponse response = new ErrorResponse(
				HttpStatus.BAD_REQUEST.value(),
				ErrorCode.DUPLICATE_RESOURCE,
				"Dữ liệu bị trùng hoặc vi phạm ràng buộc toàn vẹn",
				List.of());
		return ResponseEntity.badRequest().body(response);
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<ErrorResponse> handleHttpMessageNotReadable() {
		ErrorResponse response = new ErrorResponse(
				HttpStatus.BAD_REQUEST.value(),
				ErrorCode.INVALID_INPUT_DATA,
				INVALID_INPUT_MESSAGE,
				List.of());
		return ResponseEntity.badRequest().body(response);
	}

	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<ErrorResponse> handleAccessDenied() {
		ErrorResponse response = new ErrorResponse(
				HttpStatus.FORBIDDEN.value(),
				ErrorCode.ACCESS_DENIED,
				"Không có quyền thực hiện thao tác này",
				List.of());
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleException() {
		ErrorResponse response = new ErrorResponse(
				HttpStatus.INTERNAL_SERVER_ERROR.value(),
				ErrorCode.INTERNAL_SERVER_ERROR,
				"Đã xảy ra lỗi hệ thống",
				List.of());
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	}

	private FieldErrorResponse toFieldError(ConstraintViolation<?> violation) {
		return new FieldErrorResponse(violation.getPropertyPath().toString(), violation.getMessage());
	}
}
