package com.prgrms.mukvengers.global.security.token.exception;

import com.prgrms.mukvengers.global.exception.ErrorCode;
import com.prgrms.mukvengers.global.exception.ServiceException;

public class NotFoundCookieException extends ServiceException {

	private static final ErrorCode ERROR_CODE = ErrorCode.NOT_FOUND_COOKIE;
	private static final String MESSAGE_KEY = "exception.user.notfound";

	public NotFoundCookieException() {
		super(ERROR_CODE, MESSAGE_KEY, new String[] {});
	}
}
