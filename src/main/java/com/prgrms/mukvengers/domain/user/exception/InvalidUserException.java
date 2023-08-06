package com.prgrms.mukvengers.domain.user.exception;

import com.prgrms.mukvengers.global.base.exception.ErrorCode;
import com.prgrms.mukvengers.global.base.exception.ServiceException;

public class InvalidUserException extends ServiceException {

	private static final ErrorCode ERROR_CODE = ErrorCode.USER_INVALID;
	private static final String MESSAGE_KEY = "exception.user.invalid";

	public InvalidUserException(Long userId) {
		super(ERROR_CODE, MESSAGE_KEY, new Object[] {userId});
	}

}
