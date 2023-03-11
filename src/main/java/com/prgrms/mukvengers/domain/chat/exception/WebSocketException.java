package com.prgrms.mukvengers.domain.chat.exception;

import com.prgrms.mukvengers.global.exception.ErrorCode;

import lombok.Getter;

@Getter
public class WebSocketException extends RuntimeException {

	private static final ErrorCode ERROR_CODE = ErrorCode.EXCEPTION_IN_WEBSOCKET;
	private static final String MESSAGE_KEY = "exception.websocket";
	private final String message;

	public WebSocketException(String message) {
		this.message = message;
	}
}
