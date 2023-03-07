package com.prgrms.mukvengers.domain.proposal.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prgrms.mukvengers.domain.proposal.dto.response.ProposalResponse;
import com.prgrms.mukvengers.domain.proposal.dto.response.ProposalResponses;
import com.prgrms.mukvengers.domain.proposal.mapper.ProposalMapper;
import com.prgrms.mukvengers.domain.proposal.repository.ProposalRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProposalServiceImpl implements ProposalService {

	private final ProposalRepository proposalRepository;
	private final ProposalMapper proposalMapper;

	@Override
	public ProposalResponses getProposalsByLeaderId(Long userId) {

		List<ProposalResponse> proposals = proposalRepository.findAllByLeaderIdOrderByCreatedAtDesc(userId)
			.stream()
			.map(proposalMapper::toProposalResponse)
			.collect(Collectors.toList());

		return new ProposalResponses(proposals);
	}

	@Override
	public ProposalResponses getProposalsByMemberId(Long userId) {

		List<ProposalResponse> proposals = proposalRepository.findAllByUserIdOrderByCreatedAtDesc(userId)
			.stream()
			.map(proposalMapper::toProposalResponse)
			.collect(Collectors.toList());

		return new ProposalResponses(proposals);
	}

}
