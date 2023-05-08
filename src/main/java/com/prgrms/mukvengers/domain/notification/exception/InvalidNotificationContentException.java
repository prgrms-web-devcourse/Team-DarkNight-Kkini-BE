package com.prgrms.mukvengers.domain.notification.exception;

import com.prgrms.mukvengers.global.exception.ErrorCode;
import com.prgrms.mukvengers.global.exception.ServiceException;

public class InvalidNotificationContentException extends ServiceException {
	private static ErrorCode errorCode = ErrorCode.INVALID_NOTIFICATION_CONTENT;
	private static String messageKey = "exception.notification.content.invalid";

	public InvalidNotificationContentException(String content) {
		super(errorCode, messageKey, new Object[] {content});
	}
}
