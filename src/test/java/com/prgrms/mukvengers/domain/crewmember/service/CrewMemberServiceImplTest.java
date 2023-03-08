package com.prgrms.mukvengers.domain.crewmember.service;

import static com.prgrms.mukvengers.domain.crew.model.vo.CrewStatus.*;
import static com.prgrms.mukvengers.domain.crewmember.model.vo.CrewMemberRole.*;
import static com.prgrms.mukvengers.utils.CrewObjectProvider.*;
import static org.assertj.core.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.prgrms.mukvengers.base.ServiceTest;
import com.prgrms.mukvengers.domain.crew.model.Crew;
import com.prgrms.mukvengers.domain.crewmember.model.CrewMember;
import com.prgrms.mukvengers.domain.crewmember.repository.CrewMemberRepository;
import com.prgrms.mukvengers.global.common.dto.IdResponse;

class CrewMemberServiceImplTest extends ServiceTest {

	@Autowired
	private CrewMemberService crewMemberService;

	@Autowired
	private CrewMemberRepository crewMemberRepository;

	@Test
	@DisplayName("[성공] 모임아이디와, 유저아이디, 모임 역할로 모임원을 생성할 수 있다.")
	void create_success() {

		//given
		Crew crew = createCrew(savedStore, RECRUITING);

		crewRepository.save(crew);

		//when
		IdResponse idResponse = crewMemberService.create(crew.getId(), savedUserId, LEADER);

		//then
		Optional<CrewMember> optionalCrewMember = crewMemberRepository.findById(idResponse.id());

		assertThat(optionalCrewMember).isPresent();

		CrewMember crewMember = optionalCrewMember.get();
		assertThat(crewMember)
			.hasFieldOrPropertyWithValue("userId", savedUserId)
			.hasFieldOrPropertyWithValue("crew", crew)
			.hasFieldOrPropertyWithValue("crewMemberRole", LEADER);
	}
}