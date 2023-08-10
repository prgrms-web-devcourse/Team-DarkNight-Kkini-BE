package com.prgrms.mukvengers.domain.proposal.service;

import org.springframework.data.domain.Pageable;

import com.prgrms.mukvengers.domain.proposal.dto.request.CreateProposalRequest;
import com.prgrms.mukvengers.domain.proposal.dto.request.UpdateProposalRequest;
import com.prgrms.mukvengers.domain.proposal.dto.response.ProposalPageResponse;
import com.prgrms.mukvengers.domain.proposal.dto.response.ProposalResponse;
import com.prgrms.mukvengers.global.base.dto.IdResponse;

public interface ProposalService {

	IdResponse create(CreateProposalRequest proposalRequest, Long userId, Long crewId);

	ProposalResponse getById(Long proposalId);

	ProposalPageResponse getProposalsByLeaderId(Long userId, Pageable pageable);

	ProposalPageResponse getProposalsByMemberId(Long userId, Pageable pageable);

	void updateProposalStatus(UpdateProposalRequest proposalRequest, Long userId, Long proposalId);

	void delete(Long proposalId, Long userId);
}