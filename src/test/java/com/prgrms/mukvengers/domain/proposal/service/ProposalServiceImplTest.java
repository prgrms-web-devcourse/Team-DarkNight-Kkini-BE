package com.prgrms.mukvengers.domain.proposal.service;

import static com.prgrms.mukvengers.domain.proposal.service.ProposalServiceImpl.*;
import static com.prgrms.mukvengers.utils.CrewObjectProvider.*;
import static com.prgrms.mukvengers.utils.ProposalObjectProvider.*;
import static com.prgrms.mukvengers.utils.UserObjectProvider.*;
import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.prgrms.mukvengers.base.ServiceTest;
import com.prgrms.mukvengers.domain.crew.model.Crew;
import com.prgrms.mukvengers.domain.crewmember.model.CrewMember;
import com.prgrms.mukvengers.domain.crewmember.model.vo.CrewMemberRole;
import com.prgrms.mukvengers.domain.proposal.dto.request.CreateProposalRequest;
import com.prgrms.mukvengers.domain.proposal.dto.response.ProposalResponse;
import com.prgrms.mukvengers.domain.proposal.dto.response.ProposalResponses;
import com.prgrms.mukvengers.domain.proposal.model.Proposal;
import com.prgrms.mukvengers.domain.user.model.User;
import com.prgrms.mukvengers.global.common.dto.IdResponse;
import com.prgrms.mukvengers.utils.CrewMemberObjectProvider;
import com.prgrms.mukvengers.utils.ProposalObjectProvider;

class ProposalServiceImplTest extends ServiceTest {

	@Test
	@DisplayName("[성공] 사용자는 신청서를 작성할 수 있다.")
	void createProposal_success() {

		//given
		User createUser = createUser("12121212");
		User leader = userRepository.save(createUser);

		Crew createCrew = createCrew(savedStore);
		Crew crew = crewRepository.save(createCrew);

		CreateProposalRequest proposalRequest = ProposalObjectProvider.createProposalRequest(leader.getId());

		// when
		IdResponse response = proposalService.create(proposalRequest, savedUserId, crew.getId());

		// then
		Optional<Proposal> findProposal = proposalRepository.findById(response.id());
		assertThat(findProposal).isPresent();
		assertThat(findProposal.get())
			.hasFieldOrPropertyWithValue("user", savedUser)
			.hasFieldOrPropertyWithValue("leaderId", leader.getId())
			.hasFieldOrPropertyWithValue("crewId", crew.getId())
			.hasFieldOrPropertyWithValue("content", proposalRequest.content());
	}

	@Test
	@DisplayName("[실패] 모집 정원이 다 찬 밥모임에는 신청서를 작성할 수 없다.")
	void createProposal_fail_countOverCapacity() {

		//given
		User createUser = createUser("12121212");
		User leader = userRepository.save(createUser);

		Crew createCrew = createCrew(savedStore);
		Crew crew = crewRepository.save(createCrew);

		CrewMember createCrewMember = CrewMemberObjectProvider.createCrewMember(leader.getId(), crew,
			CrewMemberRole.LEADER);
		crewMemberRepository.save(createCrewMember);

		List<CrewMember> crewMembers = CrewMemberObjectProvider.createCrewMembers(savedUserId, crew,
			CrewMemberRole.MEMBER,
			crew.getCapacity());

		crewMemberRepository.saveAll(crewMembers);

		CreateProposalRequest proposalRequest = ProposalObjectProvider.createProposalRequest(leader.getId());

		// when & then
		assertThatThrownBy
			(
				() -> proposalService.create(proposalRequest, savedUserId, crew.getId())
			)
			.isInstanceOf(IllegalStateException.class)
			.hasMessageContaining(CREW_MEMBER_COUNT_OVER_CAPACITY_EXCEPTION_MESSAGE);
	}

	@Test
	@DisplayName("[실패] 해당 밥모임에 강퇴된 사용자라면 신청서를 작성할 수 없다.")
	void createProposal_fail_blockedUser() {

		//given
		Crew createCrew = createCrew(savedStore);
		Crew crew = crewRepository.save(createCrew);

		CrewMember createCrewMember = CrewMemberObjectProvider.createCrewMember(savedUserId, crew,
			CrewMemberRole.BLOCKED);
		crewMemberRepository.save(createCrewMember);

		CreateProposalRequest proposalRequest = ProposalObjectProvider.createProposalRequest(savedUserId);

		// when & then
		assertThatThrownBy
			(
				() -> proposalService.create(proposalRequest, savedUserId, crew.getId())
			)
			.isInstanceOf(IllegalStateException.class)
			.hasMessageContaining(DISMISSED_USER_EXCEPTION_MESSAGE);
	}

	@Test
	@DisplayName("[실패] 사용자가 해당 밥모임의 리더라면 신청서를 작성할 수 없다.")
	void createProposal_fail_LeaderUser() {

		//given
		Crew createCrew = createCrew(savedStore);
		Crew crew = crewRepository.save(createCrew);

		CrewMember createCrewMember = CrewMemberObjectProvider.createCrewMember(savedUserId, crew,
			CrewMemberRole.LEADER);
		crewMemberRepository.save(createCrewMember);

		CreateProposalRequest proposalRequest = ProposalObjectProvider.createProposalRequest(savedUserId);

		// when & then
		assertThatThrownBy
			(
				() -> proposalService.create(proposalRequest, savedUserId, crew.getId())
			)
			.isInstanceOf(IllegalStateException.class)
			.hasMessageContaining(LEADER_USER_EXCEPTION_MESSAGE);
	}

	@Test
	@DisplayName("[실패] 사용자가 이미 해당 밥모임에 참여자라면 신청서를 작성할 수 없다.")
	void createProposal_fail_DuplicatedUser() {

		//given
		Crew createCrew = createCrew(savedStore);
		Crew crew = crewRepository.save(createCrew);

		CrewMember createCrewMember = CrewMemberObjectProvider.createCrewMember(savedUserId, crew,
			CrewMemberRole.MEMBER);
		crewMemberRepository.save(createCrewMember);

		CreateProposalRequest proposalRequest = ProposalObjectProvider.createProposalRequest(savedUserId);

		// when & then
		assertThatThrownBy
			(
				() -> proposalService.create(proposalRequest, savedUserId, crew.getId())
			)
			.isInstanceOf(IllegalStateException.class)
			.hasMessageContaining(DUPLICATE_USER_EXCEPTION_MESSAGE);
	}

	@Test
	@DisplayName("[성공] 신청서 아이디로 신청서를 조회한다.")
	void getById() {
		//given
		User user = createUser("1232456789");
		userRepository.save(user);

		Crew crew = createCrew(savedStore);
		crewRepository.save(crew);

		Proposal proposal = ProposalObjectProvider.createProposal(user, savedUser.getId(), crew.getId());
		proposalRepository.save(proposal);

		//when
		ProposalResponse response = proposalService.getById(proposal.getId());

		assertThat(response)
			.hasFieldOrPropertyWithValue("id", proposal.getId())
			.hasFieldOrPropertyWithValue("leaderId", savedUserId)
			.hasFieldOrPropertyWithValue("crewId", crew.getId())
			.hasFieldOrPropertyWithValue("content", proposal.getContent())
			.hasFieldOrPropertyWithValue("status", proposal.getStatus());

		assertThat(response.user())
			.hasFieldOrPropertyWithValue("id", user.getId())
			.hasFieldOrPropertyWithValue("nickname", user.getNickname())
			.hasFieldOrPropertyWithValue("profileImgUrl", user.getProfileImgUrl())
			.hasFieldOrPropertyWithValue("introduction", user.getIntroduction())
			.hasFieldOrPropertyWithValue("leaderCount", user.getLeaderCount())
			.hasFieldOrPropertyWithValue("crewCount", user.getCrewCount())
			.hasFieldOrPropertyWithValue("tasteScore", user.getTasteScore())
			.hasFieldOrPropertyWithValue("mannerScore", user.getMannerScore());
	}

	@Test
	@DisplayName("[성공] 사용자가 방장인 모임의 모든 신청서를 조회한다.")
	void getProposalsByLeaderId_success() {

		//given
		User user = createUser("1232456789");
		userRepository.save(user);

		Crew crew = createCrew(savedStore);
		crewRepository.save(crew);

		List<Proposal> proposals = createProposals(user, savedUser.getId(), crew.getId());
		proposalRepository.saveAll(proposals);

		//when
		ProposalResponses responses = proposalService.getProposalsByLeaderId(savedUser.getId());

		//then
		assertThat(responses.responses()).hasSize(proposals.size());
	}

	@Test
	@DisplayName("[성공] 사용자가 방장인 아니고 참여자인 밥모임의 신청서를 모두 조회합니다.")
	void getProposalsByMemberId_success() {

		//given
		User user = createUser("1232456789");
		userRepository.save(user);

		Crew crew = createCrew(savedStore);
		crewRepository.save(crew);

		List<Proposal> proposals = createProposals(savedUser, user.getId(), crew.getId());
		proposalRepository.saveAll(proposals);

		//when
		ProposalResponses responses = proposalService.getProposalsByMemberId(savedUser.getId());

		//then
		assertThat(responses.responses()).hasSize(proposals.size());
	}

}