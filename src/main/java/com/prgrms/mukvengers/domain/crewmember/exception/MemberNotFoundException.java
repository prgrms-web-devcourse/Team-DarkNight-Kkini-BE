package com.prgrms.mukvengers.domain.crewmember.exception;

import com.prgrms.mukvengers.global.exception.ErrorCode;
import com.prgrms.mukvengers.global.exception.ServiceException;

public class MemberNotFoundException extends ServiceException {

	private static final ErrorCode ERROR_CODE = ErrorCode.MEMBER_NOT_FOUND;
	private static final String MESSAGE_KEY = "exception.crewMember.member.notfound";

	public MemberNotFoundException(Long userId) {
		super(ERROR_CODE, MESSAGE_KEY, new Object[] {userId});
	}
}