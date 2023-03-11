package com.prgrms.mukvengers.global.exception;

import static org.springframework.http.HttpStatus.*;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestValueException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import net.gpedro.integrations.slack.SlackApi;

import com.prgrms.mukvengers.domain.chat.exception.WebSocketException;
import com.prgrms.mukvengers.global.common.annotation.SlackNotification;
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

	private final SlackApi slackApi;

	public GlobalExceptionHandler(@Value("${spring.slack.webhook}") String webhook) {
		this.slackApi = new SlackApi(webhook);
	}

	@ExceptionHandler(WebSocketException.class) // custom 에러
	public ResponseEntity<Void> handleServiceException(WebSocketException e) {
		log.error(e.getMessage());
		return ResponseEntity.noContent().build();
	}

	@ExceptionHandler(ServiceException.class) // custom 에러
	public ResponseEntity<ErrorResponse> handleServiceException(HttpServletRequest request, ServiceException e) {
		ErrorCode errorCode = e.getErrorCode();
		logService(request, e, errorCode);
		return ResponseEntity
			.status(errorCode.getStatus())
			.body(ErrorResponse.of(errorCode));
	}

	@ResponseStatus(BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class) // Bean Validation(@Valid)
	public ErrorResponse handleMethodArgumentNotValidException(HttpServletRequest request, BindException e) {
		logDebug(request, e);
		log.debug("[EXCEPTION] FIELD_ERROR       -----> [{}]", e.getFieldError());
		return ErrorResponse.of(ErrorCode.INVALID_INPUT_VALUE);
	}

	@ResponseStatus(BAD_REQUEST)
	@ExceptionHandler(MissingRequestValueException.class) // 요청 데이터로 들어와야할 인자 부족
	public ErrorResponse handleMissingRequestValueException(HttpServletRequest request,
		MissingRequestValueException e) {
		logDebug(request, e);
		return ErrorResponse.of(ErrorCode.MISSING_INPUT_VALUE);
	}

	@ResponseStatus(BAD_REQUEST)
	@ExceptionHandler(HttpRequestMethodNotSupportedException.class) // 해당 uri에 잘못된 HttpMethod
	public ErrorResponse handleHttpRequestMethodNotSupportedException(HttpServletRequest request,
		HttpRequestMethodNotSupportedException e) {
		logDebug(request, e);
		return ErrorResponse.of(ErrorCode.METHOD_NOT_ALLOWED);
	}

	@ResponseStatus(BAD_REQUEST)
	@ExceptionHandler(NoHandlerFoundException.class) // 없는 api(uri)
	public ErrorResponse handleNoHandlerFoundException(HttpServletRequest request, NoHandlerFoundException e) {
		logDebug(request, e);
		return ErrorResponse.of(ErrorCode.NOT_EXIST_API);
	}

	@ResponseStatus(BAD_REQUEST)
	@ExceptionHandler(IllegalArgumentException.class) // 메소드 validation 예외 상황
	public ErrorResponse handleIllegalArgumentException(IllegalArgumentException e) {
		logWarn(e);
		return ErrorResponse.of(ErrorCode.INVALID_INPUT_VALUE);
	}

	@SlackNotification
	@ResponseStatus(INTERNAL_SERVER_ERROR)
	@ExceptionHandler(Exception.class)
	public ErrorResponse handleException(HttpServletRequest request, Exception e) {
		logError(e);
		return ErrorResponse.of(ErrorCode.INTERNAL_SERVER_ERROR);
	}

	private void logService(HttpServletRequest request, ServiceException e, ErrorCode errorCode) {
		log.debug(EXCEPTION_REQUEST_URI, request.getRequestURI());
		log.debug(EXCEPTION_HTTP_METHOD_TYPE, request.getMethod());
		log.debug(MessageUtil.getMessage(e.getMessageKey()));
		log.info(errorCode.getCode());
		log.warn(EXCEPTION_TYPE_FORMAT, e.getClass().getSimpleName());
		log.warn(EXCEPTION_MESSAGE_FORMAT, e.getMessage());
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

