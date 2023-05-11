package com.prgrms.mukvengers.domain.notification.exception;

import com.prgrms.mukvengers.global.exception.ErrorCode;
import com.prgrms.mukvengers.global.exception.ServiceException;

public class InvalidNotificationAccessException extends ServiceException {
	private static ErrorCode errorCode = ErrorCode.INVALID_NOTIFICATION_ACCESS;
	private static String messageKey = "exception.notification.access.invalid";

	public InvalidNotificationAccessException(Long notificationId) {
		super(errorCode, messageKey, new Object[] {notificationId});
	}

	public InvalidNotificationAccessException(Long userId, Long notificationId) {
		super(errorCode, messageKey, new Object[] {userId, notificationId});
	}
}
