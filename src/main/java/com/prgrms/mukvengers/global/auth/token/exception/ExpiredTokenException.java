package com.prgrms.mukvengers.global.auth.token.exception;

import com.prgrms.mukvengers.global.base.exception.ErrorCode;

public class ExpiredTokenException extends TokenException {

	private static final ErrorCode ERROR_CODE = ErrorCode.EXPIRED_TOKEN;
	private static final String MESSAGE_KEY = "exception.token.expired";

	public ExpiredTokenException() {
		super(ERROR_CODE, MESSAGE_KEY);
	}

}