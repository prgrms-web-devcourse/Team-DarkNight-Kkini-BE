package com.prgrms.mukvengers.domain.proposal.mapper;

import org.mapstruct.Mapper;

import com.prgrms.mukvengers.domain.proposal.dto.request.CreateProposalRequest;
import com.prgrms.mukvengers.domain.proposal.dto.response.ProposalResponse;
import com.prgrms.mukvengers.domain.proposal.model.Proposal;
import com.prgrms.mukvengers.domain.user.model.User;

@Mapper(componentModel = "spring")
public interface ProposalMapper {

	Proposal toProposal(CreateProposalRequest proposalRequest, User user, Long crewId);

	ProposalResponse toProposalResponse(Proposal proposal);
}
