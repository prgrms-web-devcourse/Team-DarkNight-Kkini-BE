package com.prgrms.mukvengers.global.auth.token.exception;

import com.prgrms.mukvengers.global.base.exception.ErrorCode;
import com.prgrms.mukvengers.global.base.exception.ServiceException;

public abstract class TokenException extends ServiceException {

	protected TokenException(ErrorCode errorCode, String messageKey) {
		super(errorCode, messageKey, null);
	}
}
