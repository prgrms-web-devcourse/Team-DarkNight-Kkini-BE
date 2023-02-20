package com.prgrms.mukvengers.global.common.dto;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonFormat;

public record ErrorResponse(
	int status,
	String message,
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
	LocalDateTime timestamp,
	List<FieldError> errors,
	String path
) {

	public record FieldError(
		String fieldName,
		String inputValue,
		String reason
	){
		public static FieldError of(String filedName, Object value, String reason) {
			return new FieldError(filedName, value.toString(), reason);
		}
	}

	public static ErrorResponse of(HttpStatus status, String message, String path,
		List<FieldError> errors) {
		return new ErrorResponse(status.value(), message, LocalDateTime.now(), errors, path);
	}

	public static ErrorResponse badRequest(String message, String path, List<FieldError> errors) {
		return new ErrorResponse(HttpStatus.BAD_REQUEST.value(), message,
			LocalDateTime.now(), errors, path);
	}

	public static ErrorResponse badRequest(String message, String path) {
		return new ErrorResponse(HttpStatus.BAD_REQUEST.value(), message,
			LocalDateTime.now(), null, path);
	}

	public static ErrorResponse notFound(String message, String path, List<FieldError> errors) {
		return new ErrorResponse(HttpStatus.NOT_FOUND.value(), message, LocalDateTime.now(), errors, path);
	}

	public static ErrorResponse unAuthorized(String message, String path, List<FieldError> errors) {
		return new ErrorResponse(HttpStatus.UNAUTHORIZED.value(), message, LocalDateTime.now(), errors, path);
	}

	public static ErrorResponse forbidden(String message, String path, List<FieldError> errors) {
		return new ErrorResponse(HttpStatus.FORBIDDEN.value(), message, LocalDateTime.now(), errors, path);
	}

}