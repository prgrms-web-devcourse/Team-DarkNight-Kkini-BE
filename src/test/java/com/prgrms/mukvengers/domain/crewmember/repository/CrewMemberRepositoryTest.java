package com.prgrms.mukvengers.domain.crewmember.repository;

import static com.prgrms.mukvengers.utils.CrewMemberObjectProvider.*;
import static com.prgrms.mukvengers.utils.CrewObjectProvider.*;
import static com.prgrms.mukvengers.utils.UserObjectProvider.*;
import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;
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
		reviewer = savedUser1;
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
		CrewMember crewMember = CrewMemberObjectProvider.createCrewMember(savedUser1Id, crew, CrewMemberRole.MEMBER);
		crewMemberRepository.save(crewMember);

		//when
		int delete = crewMemberRepository.deleteByUserIdAndCrewId(savedUser1Id, crew.getId());

		//then
		assertThat(delete).isEqualTo(1);

	}

	@Test
	@DisplayName("[성공] 사용자 아이디로 사용자가 참여한 모임을 약속시간이 빠른순으로 조회한다.")
	void findAllByUserIdAndNotBlocked_success() {

		//given
		Crew crew1 = CrewObjectProvider.createCrew(savedStore, LocalDateTime.now().minusDays(1L));
		Crew crew2 = CrewObjectProvider.createCrew(savedStore, LocalDateTime.now().plusDays(1L));
		Crew crew3 = CrewObjectProvider.createCrew(savedStore, LocalDateTime.now());
		CrewMember crewMember1 = CrewMemberObjectProvider.createCrewMember(savedUser1Id, crew1, CrewMemberRole.MEMBER);
		CrewMember crewMember2 = CrewMemberObjectProvider.createCrewMember(savedUser1Id, crew2, CrewMemberRole.MEMBER);
		CrewMember crewMember3 = CrewMemberObjectProvider.createCrewMember(savedUser1Id, crew3, CrewMemberRole.MEMBER);
		crew1.addCrewMember(crewMember1);
		crew2.addCrewMember(crewMember2);
		crew3.addCrewMember(crewMember3);
		crewRepository.save(crew1);
		crewRepository.save(crew2);
		crewRepository.save(crew3);

		//when
		List<Crew> crews = crewMemberRepository.findAllByUserIdAndNotBlockedOrderByPromiseTime(savedUser1Id);

		for (int i = 0; i < crews.size() - 1; i++) {
			assertThat(crews.get(i).getPromiseTime()).isBefore(crews.get(i + 1).getPromiseTime());
		}

	}
}