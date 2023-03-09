package com.prgrms.mukvengers.domain.crew.exception;

import com.prgrms.mukvengers.domain.crew.model.vo.CrewStatus;
import com.prgrms.mukvengers.global.exception.ErrorCode;
import com.prgrms.mukvengers.global.exception.ServiceException;

public class CrewStatusException extends ServiceException {

	private static final ErrorCode ERROR_CODE = ErrorCode.CREW_ILLEGAL_STATUS;
	private static final String MESSAGE_KEY = "exception.crew.notfound";

	public CrewStatusException(CrewStatus status) {
		super(ERROR_CODE, MESSAGE_KEY, new Object[] {status});
	}
}
