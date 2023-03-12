package com.prgrms.mukvengers.domain.proposal.exception;

import com.prgrms.mukvengers.global.exception.ErrorCode;
import com.prgrms.mukvengers.global.exception.ServiceException;

public class DuplicateProposalException extends ServiceException {

	private static final ErrorCode ERROR_CODE = ErrorCode.DUPLICATE_PROPOSAL;
	private static final String MESSAGE_KEY = "exception.duplicate.proposal";

	public DuplicateProposalException(Long proposalId) {
		super(ERROR_CODE, MESSAGE_KEY, new Object[] {proposalId});
	}
}
