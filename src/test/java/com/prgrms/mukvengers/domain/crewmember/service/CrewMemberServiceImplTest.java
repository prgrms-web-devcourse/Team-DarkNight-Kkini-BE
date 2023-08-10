package com.prgrms.mukvengers.domain.crewmember.service;

import static com.prgrms.mukvengers.domain.crewmember.model.vo.CrewMemberRole.*;
import static com.prgrms.mukvengers.utils.CrewMemberObjectProvider.*;
import static com.prgrms.mukvengers.utils.CrewObjectProvider.*;
import static org.assertj.core.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.prgrms.mukvengers.base.ServiceTest;
import com.prgrms.mukvengers.domain.crew.model.Crew;
import com.prgrms.mukvengers.domain.crewmember.exception.NotLeaderException;
import com.prgrms.mukvengers.domain.crewmember.exception.NotMemberException;
import com.prgrms.mukvengers.domain.crewmember.model.CrewMember;
import com.prgrms.mukvengers.domain.crewmember.repository.CrewMemberRepository;
import com.prgrms.mukvengers.global.base.dto.IdResponse;

class CrewMemberServiceImplTest extends ServiceTest {

	@Autowired
	private CrewMemberService crewMemberService;

	@Autowired
	private CrewMemberRepository crewMemberRepository;

	@Test
	@DisplayName("[성공] 모임아이디와, 유저아이디, 모임 역할로 모임원을 생성할 수 있다.")
	void create_success() {

		//given
		Crew crew = createCrew(savedStore);

		crewRepository.save(crew);

		//when
		Long crewMemberId = crewMemberService.create(crew, savedUser1Id, LEADER);

		//then
		Optional<CrewMember> optionalCrewMember = crewMemberRepository.findById(crewMemberId);

		assertThat(optionalCrewMember).isPresent();

		CrewMember crewMember = optionalCrewMember.get();
		assertThat(crewMember)
			.hasFieldOrPropertyWithValue("userId", savedUser1Id)
			.hasFieldOrPropertyWithValue("crew", crew)
			.hasFieldOrPropertyWithValue("crewMemberRole", LEADER);
	}

	@Test
	@DisplayName("[성공] 사용자 아이디, 강퇴할 사용자 아이디, 모임 아이디로 사용자를 강퇴한다.")
	void block_success() {

		//given
		Crew crew = createCrew(savedStore);
		crewRepository.save(crew);

		CrewMember leader = createCrewMember(savedUser1Id, crew, LEADER);
		CrewMember member = createCrewMember(savedUser2Id, crew, MEMBER);

		crewMemberRepository.save(leader);
		crewMemberRepository.save(member);

		//when
		crewMemberService.block(leader.getUserId(), member.getUserId(), crew.getId());

		//then
		assertThat(member.getCrewMemberRole()).isEqualTo(BLOCKED);
	}

	@Test
	@DisplayName("[성공] 강퇴요청은 방장이 아닌경우 실패한다.")
	void block_fail_NotLeader() {

		//given
		Crew crew = createCrew(savedStore);
		crewRepository.save(crew);

		CrewMember member1 = createCrewMember(savedUser1Id, crew, MEMBER);
		CrewMember member2 = createCrewMember(savedUser2Id, crew, MEMBER);

		crewMemberRepository.save(member1);
		crewMemberRepository.save(member2);

		//when & then
		assertThatThrownBy(
			() -> crewMemberService.block(member1.getUserId(), member2.getUserId(), crew.getId())).isInstanceOf(
			NotLeaderException.class);
	}

	@Test
	@DisplayName("[성공] 강퇴 당하는 사람이 멤버가 아닌경우 실패한다.")
	void block_fail_NotMember() {

		//given
		Crew crew = createCrew(savedStore);
		crewRepository.save(crew);

		CrewMember leader = createCrewMember(savedUser1Id, crew, LEADER);

		crewMemberRepository.save(leader);

		//when & then
		assertThatThrownBy(
			() -> crewMemberService.block(leader.getUserId(), leader.getUserId(), crew.getId())).isInstanceOf(
			NotMemberException.class);
	}

}