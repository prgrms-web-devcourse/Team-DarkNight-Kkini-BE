package com.prgrms.mukvengers.domain.review.exception;

import com.prgrms.mukvengers.global.exception.ErrorCode;
import com.prgrms.mukvengers.global.exception.ServiceException;

public class ReviewNotAccessException extends ServiceException {

	private static final ErrorCode ERROR_CODE = ErrorCode.REVIEW_NO_ACCESS;
	private static final String MESSAGE_KEY = "exception.review.no.access";

	public ReviewNotAccessException(Long userId) {
		super(ERROR_CODE, MESSAGE_KEY, new Object[] {userId});
	}
}
