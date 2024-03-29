package com.prgrms.mukvengers.domain.crew.service;

import static com.prgrms.mukvengers.domain.crew.model.vo.CrewStatus.*;
import static com.prgrms.mukvengers.utils.CrewMemberObjectProvider.*;
import static com.prgrms.mukvengers.utils.CrewObjectProvider.*;
import static com.prgrms.mukvengers.utils.StoreObjectProvider.*;
import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import com.prgrms.mukvengers.base.ServiceTest;
import com.prgrms.mukvengers.domain.crew.dto.request.CreateCrewRequest;
import com.prgrms.mukvengers.domain.crew.dto.request.SearchCrewRequest;
import com.prgrms.mukvengers.domain.crew.dto.response.CrewDetailResponse;
import com.prgrms.mukvengers.domain.crew.dto.response.CrewLocationResponse;
import com.prgrms.mukvengers.domain.crew.dto.response.CrewLocationResponses;
import com.prgrms.mukvengers.domain.crew.dto.response.CrewPageResponse;
import com.prgrms.mukvengers.domain.crew.dto.response.CrewResponses;
import com.prgrms.mukvengers.domain.crew.dto.response.MyCrewResponse;
import com.prgrms.mukvengers.domain.crew.model.Crew;
import com.prgrms.mukvengers.domain.crewmember.model.CrewMember;
import com.prgrms.mukvengers.domain.crewmember.model.vo.CrewMemberRole;
import com.prgrms.mukvengers.domain.store.model.Store;
import com.prgrms.mukvengers.global.base.dto.IdResponse;
import com.prgrms.mukvengers.utils.CrewObjectProvider;

class CrewServiceImplTest extends ServiceTest {

	@Test
	@DisplayName("[성공] 유저 아이디로 유저가 참여한 Crew를 조회한다.")
	void getByUserId_success() {

		List<Crew> crews = createCrews(savedStore);

		crews.forEach(crew -> {
			CrewMember crewMember = createCrewMember(savedUser1Id, crew, CrewMemberRole.MEMBER);
			crew.addCrewMember(crewMember);
		});
		crewRepository.saveAll(crews);

		CrewResponses<MyCrewResponse> responses = crewService.getByUserId(savedUser1Id);
		assertThat(responses.responses()).hasSize(crews.size());
	}

	@Test
	@DisplayName("[성공] 모임 아이디로 Crew를 단건 조회한다.")
	void getById_success() {

		//given
		Crew crew = CrewObjectProvider.createCrew(savedStore);

		crewRepository.save(crew);

		//when
		CrewDetailResponse response = crewService.getById(savedUser1Id, crew.getId());

		//then
		assertThat(response)
			.hasFieldOrPropertyWithValue("id", crew.getId())
			.hasFieldOrPropertyWithValue("name", crew.getName())
			.hasFieldOrPropertyWithValue("capacity", crew.getCapacity())
			.hasFieldOrPropertyWithValue("crewStatus", crew.getStatus())
			.hasFieldOrPropertyWithValue("content", crew.getContent())
			.hasFieldOrPropertyWithValue("category", crew.getCategory())
			.hasFieldOrPropertyWithValue("promiseTime", crew.getPromiseTime());
	}

	@Test
	@DisplayName("[성공] map api 아이디로 Crew 조회를 한다")
	void findByPlaceId_success() {

		//given
		List<Crew> crews = CrewObjectProvider.createCrews(savedStore);

		crewRepository.saveAll(crews);

		Integer page = 0;
		Integer size = 5;
		Pageable pageable = PageRequest.of(page, size);

		//when
		CrewPageResponse crewSliceResponse = crewService.getByPlaceId(savedUser1Id, savedStore.getPlaceId(), pageable);

		//then
		Slice<CrewDetailResponse> responses = crewSliceResponse.responses();
		assertThat(responses).hasSize(size);

	}

	@Test
	@DisplayName("[성공] 사용자의 위치를 위경도로 받아 거리 안에 있는 밥 모임을 조회한다.")
	void findByLocation_success() {

		//given
		Crew crew = CrewObjectProvider.createCrew(savedStore);

		crewRepository.save(crew);

		Double latitude = 35.75413579;
		Double longitude = -147.4654321321;
		Integer distance = 500;

		SearchCrewRequest distanceRequest = new SearchCrewRequest(longitude, latitude, distance);

		//when
		CrewLocationResponses crewLocationResponse = crewService.getByLocationWithIndex(distanceRequest);

		//then
		List<CrewLocationResponse> responses = crewLocationResponse.responses();
		assertThat(responses).hasSize(1);

	}

	@Test
	@DisplayName("[성공] 모임의 상태를 받아 변경한다.")
	void closeStatus_success() {

		//given
		Crew crew = CrewObjectProvider.createCrew(savedStore);

		crewRepository.save(crew);

		CrewMember crewMember = createCrewMember(savedUser1Id, crew, CrewMemberRole.LEADER);

		crewMemberRepository.save(crewMember);

		//when
		crewService.updateStatus(crew.getId(), savedUser1Id, CLOSE);

		//then
		Optional<Crew> optionalCrew = crewRepository.findById(crew.getId());

		assertThat(optionalCrew).isPresent();
		Crew savedCrew = optionalCrew.get();
		assertThat(savedCrew.getStatus()).isEqualTo(CLOSE);
	}

}