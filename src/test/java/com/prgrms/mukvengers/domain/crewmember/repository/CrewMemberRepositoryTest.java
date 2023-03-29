package com.prgrms.mukvengers.domain.crewmember.repository;

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
import com.prgrms.mukvengers.domain.crewmember.model.vo.CrewMemberRole;
import com.prgrms.mukvengers.domain.user.model.User;
import com.prgrms.mukvengers.utils.CrewMemberObjectProvider;
import com.prgrms.mukvengers.utils.CrewObjectProvider;

class CrewMemberRepositoryTest extends RepositoryTest {

	User reviewer;
	User reviewee;
	Crew crew;

	@BeforeEach
	void setCrewMember() {
		reviewer = savedUser;
		reviewee = userRepository.save(createUser("kakao_1212"));
		crew = crewRepository.save(createCrew(savedStore));
	}

	@Test
	@DisplayName("[성공] 해당 리더는 밥모임에 참여한 사용자이어야한다.")
	void findCrewMemberByCrewIdAndUserIdOfLeader_success() {
		// given
		CrewMember crewMemberOfLeader = createCrewMember(reviewee.getId(), crew, CrewMemberRole.LEADER);
		CrewMember leader = crewMemberRepository.save(crewMemberOfLeader);
		crew.addCrewMember(leader);

		// when
		Optional<CrewMember> result = crewMemberRepository.findCrewMemberByCrewIdAndUserId(
			crew.getId(), leader.getUserId());

		// then
		assertThat(result).isPresent();
		assertThat(result.get().getCrewMemberRole()).isEqualTo(CrewMemberRole.LEADER);
	}

	@Test
	@DisplayName("[성공] 방장이 아닌 해당 멤버는 밥모임에 참여한 사용자이어야한다")
	void findCrewMemberByCrewIdAndUserIdOfMember_success() {
		// given
		CrewMember crewMemberOfMember = createCrewMember(reviewee.getId(), crew, CrewMemberRole.MEMBER);
		CrewMember member = crewMemberRepository.save(crewMemberOfMember);
		crew.addCrewMember(member);

		// when
		Optional<CrewMember> result = crewMemberRepository.findCrewMemberByCrewIdAndUserId(
			crew.getId(), member.getUserId());

		// then
		assertThat(result).isPresent();
		assertThat(result.get().getCrewMemberRole()).isEqualTo(CrewMemberRole.MEMBER);
	}

	@Test
	@DisplayName("[실패] 해당 밥모임에 참여하지 않은 사용자이면 empty 반환된다.")
	void findCrewMemberByCrewIdAndUserId_success() {
		// given
		Crew otherCrew = crewRepository.save(createCrew(savedStore));
		CrewMember crewMemberOfMember = createCrewMember(reviewee.getId(), otherCrew, CrewMemberRole.MEMBER);
		CrewMember member = crewMemberRepository.save(crewMemberOfMember);
		otherCrew.addCrewMember(member);

		// when
		Optional<CrewMember> result = crewMemberRepository.findCrewMemberByCrewIdAndUserId(
			crew.getId(), member.getUserId());

		// then
		assertThat(result).isEmpty();
	}

	@Test
	@DisplayName("[성공] 모임원 아이디로 모임원을 삭제한다")
	void deleteByUserId_success() {

		//given
		Crew crew = CrewObjectProvider.createCrew(savedStore);
		crewRepository.save(crew);
		CrewMember crewMember = CrewMemberObjectProvider.createCrewMember(savedUserId, crew, CrewMemberRole.MEMBER);
		crewMemberRepository.save(crewMember);

		//when
		crewMemberRepository.deleteByUserIdAndCrewId(savedUserId, crew.getId());

		//then
		Optional<CrewMember> optionalCrewMember = crewMemberRepository.findById(crewMember.getId());
		assertThat(optionalCrewMember).isEmpty();

	}
}