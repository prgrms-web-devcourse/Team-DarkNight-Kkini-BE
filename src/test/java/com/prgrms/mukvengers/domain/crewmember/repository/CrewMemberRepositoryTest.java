package com.prgrms.mukvengers.domain.crewmember.repository;

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
import com.prgrms.mukvengers.domain.user.model.User;

class CrewMemberRepositoryTest extends RepositoryTest {

	User reviewer;
	User reviewee;
	Crew crew;
	CrewMember crewMember;

	@BeforeEach
	void setCrewMember() {
		reviewer = userRepository.save(createUser());
		reviewee = userRepository.save(createUser());

		crew = crewRepository.save(createCrew(savedStore));

		// CrewMemberObjectProvider
		CrewMember createCrewMember = CrewMember.builder()
			.userId(reviewer.getId())
			.crew(crew)
			.blocked(false)
			.ready(false)
			.build();

		crewMember = crewMemberRepository.save(createCrewMember);
	}

	@Test
	@DisplayName("[성공] Reviewer와 Reviewee는 밥모임 아이디가 같아야한다.")
	void joinCrewMemberByCrewId_success() {

		Optional<Crew> findcrew = crewRepository.joinCrewMemberByCrewId(crew.getId());

		assertThat(findcrew).isPresent();
		assertThat(findcrew.get().getId()).isEqualTo(crew.getId());
	}
}