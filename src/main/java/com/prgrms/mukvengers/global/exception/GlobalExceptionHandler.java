package com.prgrms.mukvengers.global.exception;

import static org.springframework.http.HttpStatus.*;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestValueException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.prgrms.mukvengers.global.common.dto.ErrorResponse;
import com.prgrms.mukvengers.global.utils.MessageUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	private static final String EXCEPTION_FORMAT = "[EXCEPTION]                   -----> ";
	private static final String EXCEPTION_MESSAGE_FORMAT = "[EXCEPTION] EXCEPTION_MESSAGE -----> [{}]";
	private static final String EXCEPTION_TYPE_FORMAT = "[EXCEPTION] EXCEPTION_TYPE    -----> [{}]";
	private static final String EXCEPTION_REQUEST_URI = "[EXCEPTION] REQUEST_URI       -----> [{}]";
	private static final String EXCEPTION_HTTP_METHOD_TYPE = "[EXCEPTION] HTTP_METHOD_TYPE  -----> [{}]";

	@ResponseStatus(BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ErrorResponse handleMethodArgumentNotValidException(HttpServletRequest request, BindException e) {
		logDebug(request, e);
		log.debug("[EXCEPTION] FIELD_ERROR       -----> [{}]", e.getFieldError());
		return ErrorResponse.badRequest("잘못된 데이터입니다.", request.getRequestURI());
	}

	@ResponseStatus(BAD_REQUEST)
	@ExceptionHandler(MissingRequestValueException.class)
	public ErrorResponse handleMissingRequestValueException(HttpServletRequest request,
		MissingRequestValueException e) {
		logDebug(request, e);
		return ErrorResponse.badRequest("데이터가 충분하지 않습니다.",request.getRequestURI());
	}

	@ResponseStatus(BAD_REQUEST)
	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public ErrorResponse handleHttpRequestMethodNotSupportedException(HttpServletRequest request,
		HttpRequestMethodNotSupportedException e) {
		logDebug(request, e);
		return ErrorResponse.badRequest("잘못된 요청입니다.", request.getRequestURI());
	}

	@ResponseStatus(BAD_REQUEST)
	@ExceptionHandler(NoHandlerFoundException.class)
	public ErrorResponse handleNoHandlerFoundException(HttpServletRequest request, NoHandlerFoundException e) {
		logDebug(request, e);
		return ErrorResponse.badRequest("잘못된 uri 입니다.", request.getRequestURI());
	}

	@ResponseStatus(BAD_REQUEST)
	@ExceptionHandler(ServiceException.class)
	public ErrorResponse handleServiceException(HttpServletRequest request, ServiceException e) {
		logDebug(request, e);
		return ErrorResponse.badRequest(MessageUtil.getMessage(e.getMessage()), request.getRequestURI());
	}

	@ResponseStatus(BAD_REQUEST)
	@ExceptionHandler(EntityNotFoundException.class) // JPA 오류
	public ErrorResponse handleEntityNotFoundException(EntityNotFoundException e) {
		logWarn(e);
		return ErrorResponse.badRequest(e.getMessage(), null);
	}

	@ResponseStatus(BAD_REQUEST)
	@ExceptionHandler(IllegalArgumentException.class) // 예상하지 못한 내부 로직 오류
	public ErrorResponse handleIllegalArgumentException(IllegalArgumentException e) {
		logWarn(e);
		return ErrorResponse.badRequest(e.getMessage(), null);
	}

	@ResponseStatus(INTERNAL_SERVER_ERROR)
	@ExceptionHandler(Exception.class)
	public ErrorResponse handleException(HttpServletRequest request, Exception e) {
		logError(e);
		return ErrorResponse.of(INTERNAL_SERVER_ERROR, "서버 내부에서 에러가 발생하였습니다.",
			request.getRequestURI(), null);
	}

	private void logDebug(HttpServletRequest request, Exception e) {
		log.debug(EXCEPTION_REQUEST_URI, request.getRequestURI());
		log.debug(EXCEPTION_HTTP_METHOD_TYPE, request.getMethod());
		log.debug(EXCEPTION_TYPE_FORMAT, e.getClass().getSimpleName());
		log.debug(EXCEPTION_MESSAGE_FORMAT, e.getMessage());
	}

	private void logWarn(Exception e) {
		log.warn(EXCEPTION_TYPE_FORMAT, e.getClass().getSimpleName());
		log.warn(EXCEPTION_MESSAGE_FORMAT, e.getMessage());
	}

	private void logError(Exception e) {
		log.error(EXCEPTION_TYPE_FORMAT, e.getClass().getSimpleName());
		log.error(EXCEPTION_MESSAGE_FORMAT, e.getMessage());
		log.error(EXCEPTION_FORMAT, e);
	}
}

