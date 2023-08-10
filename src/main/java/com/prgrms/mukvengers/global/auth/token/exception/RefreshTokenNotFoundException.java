package com.prgrms.mukvengers.global.auth.token.exception;

import com.prgrms.mukvengers.global.base.exception.ErrorCode;
import com.prgrms.mukvengers.global.base.exception.ServiceException;

public class RefreshTokenNotFoundException extends ServiceException {

	private static final ErrorCode ERROR_CODE = ErrorCode.NOT_FOUND_REFRESH_TOKEN;
	private static final String MESSAGE_KEY = "exception.user.notfound";

	public RefreshTokenNotFoundException() {
		super(ERROR_CODE, MESSAGE_KEY, new String[] {});
	}

}