package com.prgrms.mukvengers.domain.proposal.exception;

import com.prgrms.mukvengers.global.exception.ErrorCode;
import com.prgrms.mukvengers.global.exception.ServiceException;

public class ApproveException extends ServiceException {

	private static final ErrorCode ERROR_CODE = ErrorCode.PROPOSAL_CAN_NOT_APPROVE_BY_OVER_CAPACITY;
	private static final String MESSAGE_KEY = "exception.crew.member.over.capacity.proposal";

	public ApproveException(String message) {
		super(ERROR_CODE, MESSAGE_KEY, new Object[] {message});
	}
}
