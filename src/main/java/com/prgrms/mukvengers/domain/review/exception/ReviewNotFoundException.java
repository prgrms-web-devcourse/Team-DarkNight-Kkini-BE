package com.prgrms.mukvengers.domain.review.exception;

import com.prgrms.mukvengers.global.exception.ErrorCode;
import com.prgrms.mukvengers.global.exception.ServiceException;

public class ReviewNotFoundException extends ServiceException {

	private static final ErrorCode ERROR_CODE = ErrorCode.REVIEW_NOT_FOUND;
	private static final String MESSAGE_KEY = "exception.review.notfound";

	public ReviewNotFoundException(Long reviewId) {
		super(ERROR_CODE, MESSAGE_KEY, new Object[] {reviewId});
	}
}