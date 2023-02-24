package com.prgrms.mukvengers.global.security.token.exception;

import com.prgrms.mukvengers.global.exception.ErrorCode;
import com.prgrms.mukvengers.global.exception.ServiceException;

public abstract class TokenException extends ServiceException {

	protected TokenException(ErrorCode errorCode, String messageKey) {
		super(errorCode, messageKey, null);
	}
}
