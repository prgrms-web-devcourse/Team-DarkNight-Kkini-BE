package com.prgrms.mukvengers.domain.crew.facade;

import static com.prgrms.mukvengers.domain.crew.model.vo.Status.*;
import static com.prgrms.mukvengers.domain.crewmember.model.vo.Role.*;
import static com.prgrms.mukvengers.utils.CrewObjectProvider.*;
import static org.assertj.core.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;

import com.prgrms.mukvengers.base.ServiceTest;
import com.prgrms.mukvengers.domain.crew.dto.request.CreateCrewRequest;
import com.prgrms.mukvengers.domain.crew.model.Crew;
import com.prgrms.mukvengers.domain.crew.model.vo.Category;
import com.prgrms.mukvengers.domain.crewmember.model.CrewMember;
import com.prgrms.mukvengers.global.common.dto.IdResponse;

class CrewFacadeServiceTest extends ServiceTest {

	@Autowired
	private CrewFacadeService crewFacadeService;

	@Test
	@DisplayName("[성공] 모임을 생성하고 해당 모임의 모임원을 생성하며 모임원은 방장 역할을 가진다.")
	void create() {
		CreateCrewRequest createCrewRequest = getCreateCrewRequest(savedStore.getMapStoreId());
		IdResponse idResponse = crewFacadeService.create(createCrewRequest, savedUserId);

		double parseLatitude = Double.parseDouble(createCrewRequest.latitude());
		double parseLongitude = Double.parseDouble(createCrewRequest.longitude());
		Point location = gf.createPoint(new Coordinate(parseLongitude, parseLatitude));

		Optional<Crew> optionalCrew = crewRepository.findById(idResponse.id());

		assertThat(optionalCrew).isPresent();

		Crew crew = optionalCrew.get();

		assertThat(crew)
			.hasFieldOrPropertyWithValue("store", savedStore)
			.hasFieldOrPropertyWithValue("name", createCrewRequest.name())
			.hasFieldOrPropertyWithValue("location", location)
			.hasFieldOrPropertyWithValue("capacity", createCrewRequest.capacity())
			.hasFieldOrPropertyWithValue("status", RECRUITING)
			.hasFieldOrPropertyWithValue("content", createCrewRequest.content())
			.hasFieldOrPropertyWithValue("category", Category.of(createCrewRequest.category()))
			.hasFieldOrPropertyWithValue("promiseTime", createCrewRequest.promiseTime());

		CrewMember crewMember = crewMemberRepository.findAll().get(0);

		assertThat(crewMember)
			.hasFieldOrPropertyWithValue("userId", savedUserId)
			.hasFieldOrPropertyWithValue("crew", crew)
			.hasFieldOrPropertyWithValue("role", LEADER);
	}

}