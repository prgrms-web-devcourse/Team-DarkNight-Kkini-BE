package com.prgrms.mukvengers.domain.proposal.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
import com.prgrms.mukvengers.domain.proposal.dto.response.ProposalPageResponse;
import com.prgrms.mukvengers.domain.proposal.dto.response.ProposalResponse;
import com.prgrms.mukvengers.domain.proposal.exception.ApproveException;
import com.prgrms.mukvengers.domain.proposal.exception.CrewMemberOverCapacity;
import com.prgrms.mukvengers.domain.proposal.exception.DuplicateProposalException;
import com.prgrms.mukvengers.domain.proposal.exception.ExistCrewMemberRoleException;
import com.prgrms.mukvengers.domain.proposal.exception.ProposalNotFoundException;
import com.prgrms.mukvengers.domain.proposal.mapper.ProposalMapper;
import com.prgrms.mukvengers.domain.proposal.model.Proposal;
import com.prgrms.mukvengers.domain.proposal.model.vo.ProposalStatus;
import com.prgrms.mukvengers.domain.proposal.repository.ProposalRepository;
import com.prgrms.mukvengers.domain.user.exception.InvalidUserException;
import com.prgrms.mukvengers.domain.user.exception.UserNotFoundException;
import com.prgrms.mukvengers.domain.user.model.User;
import com.prgrms.mukvengers.domain.user.repository.UserRepository;
import com.prgrms.mukvengers.global.common.dto.IdResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProposalServiceImpl implements ProposalService {

	public static final String CREW_MEMBER_COUNT_OVER_CAPACITY_EXCEPTION_MESSAGE = "해당 밥모임의 정원이 초과되어 신청서를 작성할 수 없습니다.";
	public static final String DISMISSED_USER_EXCEPTION_MESSAGE = "강퇴된 밥모임에는 신청서를 작성할 수 없습니다.";
	public static final String LEADER_USER_EXCEPTION_MESSAGE = "해당 밥모임의 리더는 신청서를 작성할 수 없습니다.";
	public static final String DUPLICATE_USER_EXCEPTION_MESSAGE = "이미 참여한 밥모임에 다시 신청서를 작성할 수 없습니다.";

	private final UserRepository userRepository;
	private final CrewRepository crewRepository;
	private final CrewMemberRepository crewMemberRepository;
	private final CrewMemberMapper crewMemberMapper;
	private final ProposalRepository proposalRepository;
	private final ProposalMapper proposalMapper;

	@Override
	@Transactional
	public IdResponse create(CreateProposalRequest proposalRequest, Long userId, Long crewId) {

		User user = userRepository.findById(userId)
			.orElseThrow(() -> new UserNotFoundException(userId));

		Crew crew = crewRepository.findById(crewId)
			.filter(c -> c.getStatus().equals(CrewStatus.RECRUITING))
			.orElseThrow(() -> new CrewNotFoundException(crewId));

		proposalRepository.findProposalByUserIdAndCrewId(userId, crewId)
			.filter(p -> p.getStatus().equals(ProposalStatus.WAITING))
			.ifPresent(proposal -> {
				throw new DuplicateProposalException(proposal.getId());
			});

		Integer currentCrewMemberCount = crewMemberRepository.countCrewMemberByCrewId(crewId);

		if (currentCrewMemberCount >= crew.getCapacity()) {
			throw new CrewMemberOverCapacity(CREW_MEMBER_COUNT_OVER_CAPACITY_EXCEPTION_MESSAGE);
		}

		Optional<CrewMember> crewMember = crewMemberRepository.findCrewMemberByCrewIdAndUserId(crewId, userId);

		if (crewMember.isPresent()) {
			switch (crewMember.get().getCrewMemberRole()) {
				case BLOCKED -> throw new ExistCrewMemberRoleException(DISMISSED_USER_EXCEPTION_MESSAGE);
				case LEADER -> throw new ExistCrewMemberRoleException(LEADER_USER_EXCEPTION_MESSAGE);
				case MEMBER -> throw new ExistCrewMemberRoleException(DUPLICATE_USER_EXCEPTION_MESSAGE);
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

		Crew crew = crewRepository.findById(proposal.getCrewId())
			.orElseThrow(() -> new CrewNotFoundException(proposal.getCrewId()));

		String crewName = crew.getName();
		String storePlaceName = crew.getStore().getPlaceName();

		return proposalMapper.toProposalResponse(proposal, crewName, storePlaceName);
	}

	@Override
	public ProposalPageResponse getProposalsByLeaderId(Long userId, Pageable pageable) {

		Page<ProposalResponse> responses = proposalRepository.findAllByLeaderId(userId, pageable)
			.map(proposal -> {
					Crew crew = crewRepository.findById(proposal.getCrewId())
						.orElseThrow(() -> new CrewNotFoundException(proposal.getCrewId()));
					String crewName = crew.getName();
					String storePlaceName = crew.getStore().getPlaceName();

					return proposalMapper.toProposalResponse(proposal, storePlaceName, crewName);
				}
			);

		return new ProposalPageResponse(responses);
	}

	@Override
	public ProposalPageResponse getProposalsByMemberId(Long userId, Pageable pageable) {

		Page<ProposalResponse> responses = proposalRepository.findAllByUserIdOrderByCreatedAtDesc(userId, pageable)
			.map(proposal -> {
				Crew crew = crewRepository.findById(proposal.getCrewId())
					.orElseThrow(() -> new CrewNotFoundException(proposal.getCrewId()));

				String crewName = crew.getName();
				String storePlaceName = crew.getStore().getPlaceName();

				return proposalMapper.toProposalResponse(proposal, storePlaceName, crewName);
			});

		return new ProposalPageResponse(responses);
	}

	@Override
	@Transactional
	public void updateProposalStatus(UpdateProposalRequest proposalRequest, Long userId, Long proposalId) {

		if (!userRepository.existsById(userId)) {
			throw new UserNotFoundException(userId);
		}

		Proposal proposal = proposalRepository.findById(proposalId)
			.orElseThrow(() -> new ProposalNotFoundException(proposalId));

		Crew crew = crewRepository.findById(proposal.getCrewId())
			.orElseThrow(() -> new CrewNotFoundException(proposal.getCrewId()));

		String status = proposalRequest.proposalStatus();

		ProposalStatus proposalStatus = ProposalStatus.of(status);

		if (proposal.isApprove(proposalStatus)) {
			if (crew.getCrewMembers().size() >= crew.getCapacity()) {
				throw new ApproveException(CREW_MEMBER_COUNT_OVER_CAPACITY_EXCEPTION_MESSAGE);
			}
			registerCrewMember(proposal, crew);
		}
		proposal.changeProposalStatus(proposalStatus);
	}

	private void registerCrewMember(Proposal proposal, Crew crew) {

		CrewMember createCrewMember = crewMemberMapper.toCrewMember(crew, proposal.getUser().getId(),
			CrewMemberRole.MEMBER);

		CrewMember crewMember = crewMemberRepository.save(createCrewMember);

		crew.addCrewMember(crewMember);
	}

	@Override
	@Transactional
	public void delete(Long proposalId, Long userId) {

		Proposal proposal = proposalRepository.findById(proposalId)
			.orElseThrow(() -> new ProposalNotFoundException(proposalId));

		if (!proposal.getUser().getId().equals(userId)) {
			throw new InvalidUserException(userId);
		}

		proposalRepository.deleteById(proposalId);

	}
}