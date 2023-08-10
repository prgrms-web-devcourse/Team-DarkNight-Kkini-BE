package com.prgrms.mukvengers.domain.crewmember.exception;

import com.prgrms.mukvengers.global.base.exception.ErrorCode;
import com.prgrms.mukvengers.global.base.exception.ServiceException;

public class CrewMemberNotFoundException extends ServiceException {

	private static final ErrorCode ERROR_CODE = ErrorCode.CREW_MEMBER_NOT_FOUND;
	private static final String MESSAGE_KEY = "exception.crewMember.notfound";

	public CrewMemberNotFoundException(Long crewMemberId) {
		super(ERROR_CODE, MESSAGE_KEY, new Object[] {crewMemberId});
	}
}
