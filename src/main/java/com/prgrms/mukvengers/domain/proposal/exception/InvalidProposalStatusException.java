package com.prgrms.mukvengers.domain.proposal.exception;

import com.prgrms.mukvengers.global.exception.ErrorCode;
import com.prgrms.mukvengers.global.exception.ServiceException;

public class InvalidProposalStatusException extends ServiceException {

	private static final ErrorCode ERROR_CODE = ErrorCode.INVALID_PROPOSAL_STATUS;
	private static final String MESSAGE_KEY = "exception.proposal.status.notfound";

	public InvalidProposalStatusException(String statusName) {
		super(ERROR_CODE, MESSAGE_KEY, new Object[] {statusName});
	}
}
