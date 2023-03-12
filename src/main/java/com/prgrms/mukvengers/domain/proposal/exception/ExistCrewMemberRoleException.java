package com.prgrms.mukvengers.domain.proposal.exception;

import com.prgrms.mukvengers.global.exception.ErrorCode;
import com.prgrms.mukvengers.global.exception.ServiceException;

public class ExistCrewMemberRoleException extends ServiceException {

	private static final ErrorCode ERROR_CODE = ErrorCode.EXIST_CREW_MEMBER_ROLE;
	private static final String MESSAGE_KEY = "exception.exist.crew.member.proposal";

	public ExistCrewMemberRoleException(String message) {
		super(ERROR_CODE, MESSAGE_KEY, new Object[] {message});
	}
}
