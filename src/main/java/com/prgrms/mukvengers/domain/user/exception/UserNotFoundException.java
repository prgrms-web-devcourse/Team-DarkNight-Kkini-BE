package com.prgrms.mukvengers.domain.user.exception;

import com.prgrms.mukvengers.global.exception.ErrorCode;
import com.prgrms.mukvengers.global.exception.ServiceException;

public class UserNotFoundException extends ServiceException {

	private static final ErrorCode errorCode = ErrorCode.NOT_FOUND_MEMBER;
	private static final String messageKey = "exception.user.notfound";

	public UserNotFoundException(Long userId) {
		super(errorCode, messageKey, new Object[] {userId});
	}

}

