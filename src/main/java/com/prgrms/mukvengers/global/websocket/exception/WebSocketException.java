package com.prgrms.mukvengers.global.websocket.exception;

import com.prgrms.mukvengers.global.exception.ErrorCode;
import com.prgrms.mukvengers.global.exception.ServiceException;

public class WebSocketException extends ServiceException {

	private static final ErrorCode ERROR_CODE = ErrorCode.EXCEPTION_IN_WEBSOCKET;
	private static final String MESSAGE_KEY = "exception.websocket";

	public WebSocketException() {
		super(ERROR_CODE, MESSAGE_KEY, null);
	}
}
