package com.prgrms.mukvengers.domain.proposal.repository;

import static com.prgrms.mukvengers.utils.CrewObjectProvider.*;
import static com.prgrms.mukvengers.utils.ProposalObjectProvider.*;
import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.prgrms.mukvengers.base.RepositoryTest;
import com.prgrms.mukvengers.domain.crew.model.Crew;
import com.prgrms.mukvengers.domain.proposal.model.Proposal;

class ProposalRepositoryTest extends RepositoryTest {

	@Test
	@DisplayName("[성공] 모임 약속 시간이 지난 신청서를 삭제한다.")
	void deleteProposalsByPromiseTime_success() {

		Crew crew = createCrew(savedStore);
		crewRepository.save(crew);

		Proposal proposal = createProposal(savedUser1, savedUser2Id, crew.getId());
		proposalRepository.save(proposal);

		int result = proposalRepository.deleteProposalsByPromiseTime(LocalDateTime.now());

		assertThat(result).isEqualTo(1);
	}
}