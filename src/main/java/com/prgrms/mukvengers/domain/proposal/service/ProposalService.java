package com.prgrms.mukvengers.domain.proposal.service;

import com.prgrms.mukvengers.domain.proposal.dto.response.ProposalResponses;

public interface ProposalService {
	ProposalResponses getProposalsByLeaderId(Long userId);

	ProposalResponses getProposalsByMemberId(Long userId);
}
