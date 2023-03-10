package com.prgrms.mukvengers.domain.crewmember.exception;

import com.prgrms.mukvengers.domain.crewmember.model.vo.CrewMemberRole;
import com.prgrms.mukvengers.global.exception.ErrorCode;
import com.prgrms.mukvengers.global.exception.ServiceException;

public class NotLeaderException extends ServiceException {
	private static final ErrorCode ERROR_CODE = ErrorCode.CREW_MEMBER_ILLEGAL_ROLE;
	private static final String MESSAGE_KEY = "exception.crewMember.illegal.role";

	public NotLeaderException(CrewMemberRole role) {
		super(ERROR_CODE, MESSAGE_KEY, new Object[] {role});
	}

}
