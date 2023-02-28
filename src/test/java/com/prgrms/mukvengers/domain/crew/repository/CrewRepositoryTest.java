package com.prgrms.mukvengers.domain.crew.repository;

import static com.prgrms.mukvengers.utils.CrewObjectProvider.*;
import static com.prgrms.mukvengers.utils.UserObjectProvider.*;
import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import com.prgrms.mukvengers.base.RepositoryTest;
import com.prgrms.mukvengers.domain.crew.model.Crew;
import com.prgrms.mukvengers.domain.crewmember.model.CrewMember;
import com.prgrms.mukvengers.domain.user.model.User;

class CrewRepositoryTest extends RepositoryTest {

	User reviewer;

	User reviewee;

	Crew crew;

	CrewMember crewMember;

	@BeforeEach
	void setCrews() {
		reviewer = savedUser;
		reviewee = userRepository.save(createUser());
	}

	@Test
	@DisplayName("[성공] 맵 api 아이디로 해당 가게의 밥 모임을 조회한다.")
	void joinStoreByMapStoreId_success() {

		Integer page = 0;

		Integer size = 5;

		Pageable pageable = PageRequest.of(page, size);

		Slice<Crew> savedCrews = crewRepository.findAllByMapStoreId(savedStore.getMapStoreId(), pageable);

		assertThat(savedCrews).hasSize(size);
	}

	@Test
	@DisplayName("[성공] 현재 사용자의 위도와 경도, 그리고 거리를 받아 거리 안에 있는 밥 모임을 조회한다.")
	void findAllByDistance_success() {

		GeometryFactory gf = new GeometryFactory();
		double longitude = -147.4654321321;
		double latitude = 35.75413579;
		Point location = gf.createPoint(new Coordinate(latitude, longitude));
		List<Crew> savedCrews = crewRepository.findAllByLocation(location, 1000);

		assertThat(savedCrews).hasSize(crews.size());

	}

	@Test
	@DisplayName("[성공] Reviewer는 리뷰를 남기고 싶은 해당 Reviewee와 밥모임 아이디가 같아야 한다.")
	void joinCrewMemberByCrewId_success() {

		crew = crewRepository.save(createCrew(reviewee, savedStore));

		CrewMember createCrewMember = CrewMember.builder()
			.user(reviewer)
			.crew(crew)
			.blocked(false)
			.ready(false)
			.build();

		crewMember = crewMemberRepository.save(createCrewMember);

		Optional<Crew> findCrew = crewRepository.joinCrewMemberByCrewId(crew.getId(), reviewer.getId(),
			reviewee.getId());

		assertThat(findCrew.get().getId()).isEqualTo(crew.getId());
		assertThat(crew.getId()).isEqualTo(crewMember.getCrew().getId());
	}

	@Test
	@DisplayName("[실패] Reviewer와 Reviewee의 밥모임 아이디가 다르면 에러가 발생한다.")
	void joinCrewMemberByCrewId_fail() {

		crew = crewRepository.save(createCrew(reviewer, savedStore));

		User otherReviewer = userRepository.save(createUser());
		Crew otherCrew = crewRepository.save(createCrew(otherReviewer, savedStore));

		CrewMember createCreMember = CrewMember.builder()
			.user(reviewee)
			.crew(otherCrew)
			.blocked(false)
			.ready(false)
			.build();

		CrewMember findCrewMember = crewMemberRepository.save(createCreMember);

		assertThat(findCrewMember.getCrew().getId()).isNotEqualTo(crew.getId());
	}
}