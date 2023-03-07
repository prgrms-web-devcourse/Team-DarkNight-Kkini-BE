package com.prgrms.mukvengers.domain.proposal.service;

import static com.prgrms.mukvengers.utils.CrewObjectProvider.*;
import static com.prgrms.mukvengers.utils.ProposalObjectProvider.*;
import static com.prgrms.mukvengers.utils.UserObjectProvider.*;
import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.prgrms.mukvengers.base.ServiceTest;
import com.prgrms.mukvengers.domain.crew.model.Crew;
import com.prgrms.mukvengers.domain.crew.model.vo.CrewStatus;
import com.prgrms.mukvengers.domain.proposal.dto.response.ProposalResponses;
import com.prgrms.mukvengers.domain.proposal.model.Proposal;
import com.prgrms.mukvengers.domain.user.model.User;

class ProposalServiceImplTest extends ServiceTest {

	@Test
	@DisplayName("[성공] 사용자가 방장인 모임의 모든 신청서를 조회한다.")
	void getProposalsByLeaderId_success() {

		//given
		User user = createUser("1232456789");
		userRepository.save(user);

		Crew crew = createCrew(savedStore, CrewStatus.RECRUITING);
		crewRepository.save(crew);

		List<Proposal> proposals = createProposals(user, savedUser.getId(), crew.getId());
		proposalRepository.saveAll(proposals);

		//when
		ProposalResponses responses = proposalService.getProposalsByLeaderId(savedUser.getId());

		//then
		assertThat(responses.responses()).hasSize(proposals.size());
	}

	@Test
	@DisplayName("[성공] 사용자가 방장인 아니고 참여자인 모임의 신청서를 모두 조회합니다.")
	void getProposalsByMemberId_success() {

		//given
		User user = createUser("1232456789");
		userRepository.save(user);

		Crew crew = createCrew(savedStore, CrewStatus.RECRUITING);
		crewRepository.save(crew);

		List<Proposal> proposals = createProposals(savedUser, user.getId(), crew.getId());
		proposalRepository.saveAll(proposals);

		//when
		ProposalResponses responses = proposalService.getProposalsByMemberId(savedUser.getId());

		//then
		assertThat(responses.responses()).hasSize(proposals.size());
	}

}