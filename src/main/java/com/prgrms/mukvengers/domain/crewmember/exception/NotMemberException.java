package com.prgrms.mukvengers.domain.crewmember.exception;

import com.prgrms.mukvengers.domain.crewmember.model.vo.CrewMemberRole;
import com.prgrms.mukvengers.global.base.exception.ErrorCode;
import com.prgrms.mukvengers.global.base.exception.ServiceException;

public class NotMemberException extends ServiceException {
	private static final ErrorCode ERROR_CODE = ErrorCode.CREW_MEMBER_ILLEGAL_ROLE;
	private static final String MESSAGE_KEY = "exception.crewMember.illegal.role";

	public NotMemberException(CrewMemberRole role) {
		super(ERROR_CODE, MESSAGE_KEY, new Object[] {role});
	}
}
