package com.prgrms.mukvengers.domain.proposal.service;

import com.prgrms.mukvengers.domain.proposal.dto.request.CreateProposalRequest;
import com.prgrms.mukvengers.domain.proposal.dto.response.ProposalResponses;
import com.prgrms.mukvengers.global.common.dto.IdResponse;

public interface ProposalService {

	IdResponse create(CreateProposalRequest proposalRequest, Long userId, Long crewId);

	ProposalResponses getProposalsByLeaderId(Long userId);

	ProposalResponses getProposalsByMemberId(Long userId);
}
