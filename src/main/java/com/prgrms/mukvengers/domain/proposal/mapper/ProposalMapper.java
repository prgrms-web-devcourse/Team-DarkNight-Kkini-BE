package com.prgrms.mukvengers.domain.proposal.mapper;

import org.mapstruct.Mapper;

import com.prgrms.mukvengers.domain.proposal.dto.response.ProposalResponse;
import com.prgrms.mukvengers.domain.proposal.model.Proposal;

@Mapper(componentModel = "spring")
public interface ProposalMapper {

	ProposalResponse toProposalResponse(Proposal proposal);
}
