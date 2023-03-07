package com.prgrms.mukvengers.domain.proposal.dto.response;

import com.prgrms.mukvengers.domain.proposal.model.vo.ProposalStatus;
import com.prgrms.mukvengers.domain.user.dto.response.UserProfileResponse;

public record ProposalResponse(
	Long id,
	UserProfileResponse user,
	Long leaderId,
	Long crewId,
	String content,
	ProposalStatus status
) {
}
