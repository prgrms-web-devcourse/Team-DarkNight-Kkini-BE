package com.prgrms.mukvengers.domain.proposal.exception;

import com.prgrms.mukvengers.global.base.exception.ErrorCode;
import com.prgrms.mukvengers.global.base.exception.ServiceException;

public class CrewMemberOverCapacity extends ServiceException {

	private static final ErrorCode ERROR_CODE = ErrorCode.PROPOSAL_OVER_CAPACITY;
	private static final String MESSAGE_KEY = "exception.crew.member.over.capacity.proposal";

	public CrewMemberOverCapacity(String message) {
		super(ERROR_CODE, MESSAGE_KEY, new Object[] {message});
	}
}
