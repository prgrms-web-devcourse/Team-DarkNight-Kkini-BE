package com.prgrms.mukvengers.domain.crewmember.repository;

import static com.prgrms.mukvengers.domain.crew.model.vo.Status.*;
import static com.prgrms.mukvengers.utils.CrewMemberObjectProvider.*;
import static com.prgrms.mukvengers.utils.CrewObjectProvider.*;
import static com.prgrms.mukvengers.utils.UserObjectProvider.*;
import static org.assertj.core.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.prgrms.mukvengers.base.RepositoryTest;
import com.prgrms.mukvengers.domain.crew.model.Crew;
import com.prgrms.mukvengers.domain.crewmember.model.CrewMember;
import com.prgrms.mukvengers.domain.crewmember.model.vo.Role;
import com.prgrms.mukvengers.domain.user.model.User;

class CrewMemberRepositoryTest extends RepositoryTest {

	User reviewer;
	User reviewee;
	Crew crew;

	@BeforeEach
	void setCrewMember() {
		reviewer = savedUser;
		reviewee = userRepository.save(createUser("kakao_1212"));
		crew = crewRepository.save(createCrew(savedStore, RECRUITING));
	}

	@Test
	@DisplayName("[성공] 해당 리더는 밥모임에 참여한 사용자이어야한다.")
	void findCrewMemberByCrewIdAndUserIdOfLeader_success() {
		// given
		CrewMember crewMemberOfLeader = createCrewMember(reviewee.getId(), crew, Role.LEADER);
		CrewMember leader = crewMemberRepository.save(crewMemberOfLeader);
		crew.addCrewMember(leader);

		// when
		Optional<CrewMember> result = crewMemberRepository.findCrewMemberByCrewIdAndUserId(
			crew.getId(), leader.getUserId());

		// then
		assertThat(result).isPresent();
		assertThat(result.get().getRole()).isEqualTo(Role.LEADER);
	}

	@Test
	@DisplayName("[성공] 방장이 아닌 해당 멤버는 밥모임에 참여한 사용자이어야한다")
	void findCrewMemberByCrewIdAndUserIdOfMember_success() {
		// given
		CrewMember crewMemberOfMember = createCrewMember(reviewee.getId(), crew, Role.MEMBER);
		CrewMember member = crewMemberRepository.save(crewMemberOfMember);
		crew.addCrewMember(member);

		// when
		Optional<CrewMember> result = crewMemberRepository.findCrewMemberByCrewIdAndUserId(
			crew.getId(), member.getUserId());

		// then
		assertThat(result).isPresent();
		assertThat(result.get().getRole()).isEqualTo(Role.MEMBER);
	}

	@Test
	@DisplayName("[실패] 해당 밥모임에 참여하지 않은 사용자이면 empty 반환된다.")
	void findCrewMemberByCrewIdAndUserId_success() {
		// given
		Crew otherCrew = crewRepository.save(createCrew(savedStore, RECRUITING));
		CrewMember crewMemberOfMember = createCrewMember(reviewee.getId(), otherCrew, Role.MEMBER);
		CrewMember member = crewMemberRepository.save(crewMemberOfMember);
		otherCrew.addCrewMember(member);

		// when
		Optional<CrewMember> result = crewMemberRepository.findCrewMemberByCrewIdAndUserId(
			crew.getId(), member.getUserId());

		// then
		assertThat(result).isEmpty();
	}
}