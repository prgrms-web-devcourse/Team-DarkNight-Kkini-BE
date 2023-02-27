package com.prgrms.mukvengers.domain.crew.exception;

import com.prgrms.mukvengers.global.exception.ErrorCode;
import com.prgrms.mukvengers.global.exception.ServiceException;

public class CrewNotFoundException extends ServiceException {

	private static final ErrorCode ERROR_CODE = ErrorCode.CREW_NOT_FOUND;
	private static final String MESSAGE_KEY = "exception.store.notfound";

	public CrewNotFoundException(Long crewId) {
		super(ERROR_CODE, MESSAGE_KEY, new Object[] {crewId});
	}
}
