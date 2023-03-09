package com.prgrms.mukvengers.domain.proposal.exception;

import com.prgrms.mukvengers.global.exception.ErrorCode;
import com.prgrms.mukvengers.global.exception.ServiceException;

public class ProposalNotFoundException extends ServiceException {

	private static final ErrorCode ERROR_CODE = ErrorCode.CREW_NOT_FOUND;
	private static final String MESSAGE_KEY = "exception.proposal.notfound";

	public ProposalNotFoundException(Long proposalId) {
		super(ERROR_CODE, MESSAGE_KEY, new Object[] {proposalId});
	}
}
