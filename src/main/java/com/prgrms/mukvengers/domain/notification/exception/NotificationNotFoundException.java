package com.prgrms.mukvengers.domain.notification.exception;

import com.prgrms.mukvengers.global.exception.ErrorCode;
import com.prgrms.mukvengers.global.exception.ServiceException;

public class NotificationNotFoundException extends ServiceException {

	private static final ErrorCode ERROR_CODE = ErrorCode.NOTIFICATION_NOT_FOUND;
	private static final String MESSAGE_KEY = "exception.notification.notExists";

	public NotificationNotFoundException(Long notificationId) {
		super(ERROR_CODE, MESSAGE_KEY, new Object[] {notificationId});
	}
}
