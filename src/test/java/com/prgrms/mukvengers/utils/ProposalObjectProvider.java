package com.prgrms.mukvengers.utils;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.prgrms.mukvengers.domain.proposal.dto.request.CreateProposalRequest;
import com.prgrms.mukvengers.domain.proposal.model.Proposal;
import com.prgrms.mukvengers.domain.user.model.User;

public class ProposalObjectProvider {

	private static final String CONTENT = "안녕하세요 저는 도라에몽 비실이 닮았습니다.";

	public static Proposal createProposal(User user, Long leaderId, Long crewId) {
		return Proposal.builder()
			.user(user)
			.leaderId(leaderId)
			.crewId(crewId)
			.content(CONTENT)
			.build();
	}

	public static List<Proposal> createProposals(User user, Long leaderId, Long crewId) {
		return IntStream.range(0, 20)
			.mapToObj(i -> createProposal(user, leaderId, crewId))
			.collect(Collectors.toList());
	}

	public static CreateProposalRequest createProposalRequest(Long userId) {
		return new CreateProposalRequest(userId, CONTENT);
	}
}
