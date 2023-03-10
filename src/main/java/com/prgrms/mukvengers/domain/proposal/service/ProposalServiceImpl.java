package com.prgrms.mukvengers.domain.proposal.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prgrms.mukvengers.domain.crew.exception.CrewNotFoundException;
import com.prgrms.mukvengers.domain.crew.model.Crew;
import com.prgrms.mukvengers.domain.crew.model.vo.CrewStatus;
import com.prgrms.mukvengers.domain.crew.repository.CrewRepository;
import com.prgrms.mukvengers.domain.crewmember.mapper.CrewMemberMapper;
import com.prgrms.mukvengers.domain.crewmember.model.CrewMember;
import com.prgrms.mukvengers.domain.crewmember.model.vo.CrewMemberRole;
import com.prgrms.mukvengers.domain.crewmember.repository.CrewMemberRepository;
import com.prgrms.mukvengers.domain.proposal.dto.request.CreateProposalRequest;
import com.prgrms.mukvengers.domain.proposal.dto.request.UpdateProposalRequest;
import com.prgrms.mukvengers.domain.proposal.dto.response.ProposalResponse;
import com.prgrms.mukvengers.domain.proposal.dto.response.ProposalResponses;
import com.prgrms.mukvengers.domain.proposal.exception.ProposalNotFoundException;
import com.prgrms.mukvengers.domain.proposal.mapper.ProposalMapper;
import com.prgrms.mukvengers.domain.proposal.model.Proposal;
import com.prgrms.mukvengers.domain.proposal.model.vo.ProposalStatus;
import com.prgrms.mukvengers.domain.proposal.repository.ProposalRepository;
import com.prgrms.mukvengers.domain.user.exception.UserNotFoundException;
import com.prgrms.mukvengers.domain.user.model.User;
import com.prgrms.mukvengers.domain.user.repository.UserRepository;
import com.prgrms.mukvengers.global.common.dto.IdResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProposalServiceImpl implements ProposalService {

	public static final String CREW_MEMBER_COUNT_OVER_CAPACITY_EXCEPTION_MESSAGE = "밥모임의 모집 인원이 마감되어 신청서를 작성할 수 없습니다.";
	public static final String DISMISSED_USER_EXCEPTION_MESSAGE = "강퇴된 밥모임에는 신청서를 작성할 수 없습니다.";
	public static final String LEADER_USER_EXCEPTION_MESSAGE = "해당 밥모임의 리더는 신청서를 작성할 수 없습니다.";
	public static final String DUPLICATE_USER_EXCEPTION_MESSAGE = "이미 신청한 밥모임에 다시 신청서를 작성할 수 없습니다.";

	private final UserRepository userRepository;
	private final CrewRepository crewRepository;
	private final CrewMemberRepository crewMemberRepository;
	private final ProposalRepository proposalRepository;
	private final ProposalMapper proposalMapper;
	private final CrewMemberMapper crewMemberMapper;

	@Override
	@Transactional
	public IdResponse create(CreateProposalRequest proposalRequest, Long userId, Long crewId) {

		User user = userRepository.findById(userId)
			.orElseThrow(() -> new UserNotFoundException(userId));

		Crew crew = crewRepository.findById(crewId)
			.filter(c -> c.getStatus().equals(CrewStatus.RECRUITING))
			.orElseThrow(() -> new CrewNotFoundException(crewId));

		Integer currentCrewMemberCount = crewMemberRepository.countCrewMemberByCrewId(crewId);

		if (currentCrewMemberCount >= crew.getCapacity()) {
			throw new IllegalStateException(CREW_MEMBER_COUNT_OVER_CAPACITY_EXCEPTION_MESSAGE);
		}

		Optional<CrewMember> crewMember = crewMemberRepository.findCrewMemberByCrewIdAndUserId(crewId, userId);

		if (crewMember.isPresent()) {
			switch (crewMember.get().getCrewMemberRole()) {
				case BLOCKED -> throw new IllegalStateException(DISMISSED_USER_EXCEPTION_MESSAGE);
				case LEADER -> throw new IllegalStateException(LEADER_USER_EXCEPTION_MESSAGE);
				case MEMBER -> throw new IllegalStateException(DUPLICATE_USER_EXCEPTION_MESSAGE);
			}
		}

		Proposal proposal = proposalMapper.toProposal(proposalRequest, user, crewId);

		Proposal saveProposal = proposalRepository.save(proposal);

		return new IdResponse(saveProposal.getId());
	}

	@Override
	public ProposalResponse getById(Long proposalId) {

		Proposal proposal = proposalRepository.findById(proposalId)
			.orElseThrow(() -> new ProposalNotFoundException(proposalId));

		return proposalMapper.toProposalResponse(proposal);
	}

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

	@Override
	@Transactional
	public void approve(UpdateProposalRequest proposalRequest, Long userId, Long proposalId) {

		if (!userRepository.existsById(userId)) {
			throw new UserNotFoundException(userId);
		}

		Proposal proposal = proposalRepository.findById(proposalId)
			.orElseThrow(() -> new ProposalNotFoundException(proposalId));

		Crew crew = crewRepository.findById(proposal.getCrewId())
			.orElseThrow(() -> new CrewNotFoundException(proposal.getCrewId()));

		String status = proposalRequest.proposalStatus();

		ProposalStatus proposalStatus = ProposalStatus.of(status);

		registerCrewMember(proposal, crew, userId, proposalStatus);
	}

	private void registerCrewMember(Proposal proposal, Crew crew, Long userId, ProposalStatus proposalStatus) {

		proposal.changeProposalStatus(proposalStatus);

		if (!proposal.isApprove(proposalStatus)) return;

		CrewMember createCrewMember = crewMemberMapper.toCrewMember(crew, userId, CrewMemberRole.MEMBER);

		CrewMember crewMember = crewMemberRepository.save(createCrewMember);

		crew.addCrewMember(crewMember);
	}
}
