package com.prgrms.mukvengers.domain.proposal.dto.response;

import org.springframework.data.domain.Page;

public record ProposalPageResponse(
	Page<ProposalResponse> responses
) {
}
