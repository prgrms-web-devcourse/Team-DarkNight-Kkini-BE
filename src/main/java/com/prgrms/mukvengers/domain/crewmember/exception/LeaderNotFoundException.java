package com.prgrms.mukvengers.domain.crewmember.exception;

import com.prgrms.mukvengers.global.exception.ErrorCode;
import com.prgrms.mukvengers.global.exception.ServiceException;

public class LeaderNotFoundException extends ServiceException {

	private static final ErrorCode ERROR_CODE = ErrorCode.LEADER_NOT_FOUND;
	private static final String MESSAGE_KEY = "exception.crewMember.leader.notfound";

	public LeaderNotFoundException(Long userId) {
		super(ERROR_CODE, MESSAGE_KEY, new Object[] {userId});
	}
}