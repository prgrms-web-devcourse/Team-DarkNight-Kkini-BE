package com.prgrms.mukvengers.domain.crew.service;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.prgrms.mukvengers.base.ServiceTest;
import com.prgrms.mukvengers.domain.crew.dto.request.CreateCrewRequest;
import com.prgrms.mukvengers.domain.crew.dto.response.CrewResponse;
import com.prgrms.mukvengers.domain.crew.dto.response.CrewResponses;
import com.prgrms.mukvengers.domain.crew.model.Crew;
import com.prgrms.mukvengers.domain.crew.model.vo.Category;
import com.prgrms.mukvengers.domain.crew.model.vo.Status;
import com.prgrms.mukvengers.domain.crew.repository.CrewRepository;
import com.prgrms.mukvengers.domain.store.model.Store;
import com.prgrms.mukvengers.domain.store.repository.StoreRepository;
import com.prgrms.mukvengers.domain.user.model.User;
import com.prgrms.mukvengers.domain.user.repository.UserRepository;
import com.prgrms.mukvengers.global.common.dto.IdResponse;
import com.prgrms.mukvengers.utils.CrewObjectProvider;
import com.prgrms.mukvengers.utils.StoreObjectProvider;
import com.prgrms.mukvengers.utils.UserObjectProvider;

class CrewServiceImplTest extends ServiceTest {

	@Autowired
	private CrewService crewService;

	@Autowired
	private CrewRepository crewRepository;

	@Autowired
	private StoreRepository storeRepository;

	@Autowired
	private UserRepository userRepository;

	@Test
	@Transactional
	@DisplayName("[성공] Crew 저장에 성공한다.")
	void create_success() {

		String mapStoreId = "16618597";

		User user = UserObjectProvider.createUser();

		userRepository.save(user);

		Store store = StoreObjectProvider.createStore(mapStoreId);

		storeRepository.save(store);

		CreateCrewRequest createCrewRequest = CrewObjectProvider.getCreateCrewRequest(mapStoreId);

		GeometryFactory gf = new GeometryFactory();
		double parseLatitude = Double.parseDouble(createCrewRequest.latitude());
		double parseLongitude = Double.parseDouble(createCrewRequest.longitude());
		Point location = gf.createPoint(new Coordinate(parseLatitude, parseLongitude));
		IdResponse idResponse = crewService.create(createCrewRequest, user.getId());

		Optional<Crew> optionalCrew = crewRepository.findById(idResponse.id());

		assertThat(crewRepository.count()).isNotZero();
		assertThat(optionalCrew).isPresent();
		Crew crew = optionalCrew.get();
		assertThat(crew)
			.hasFieldOrPropertyWithValue("leader", user)
			.hasFieldOrPropertyWithValue("store", store)
			.hasFieldOrPropertyWithValue("name", createCrewRequest.name())
			.hasFieldOrPropertyWithValue("location", location)
			.hasFieldOrPropertyWithValue("capacity", createCrewRequest.capacity())
			.hasFieldOrPropertyWithValue("status", Status.getStatus(createCrewRequest.status()))
			.hasFieldOrPropertyWithValue("content", createCrewRequest.content())
			.hasFieldOrPropertyWithValue("category", Category.getCategory(createCrewRequest.category()));
	}

	@Test
	@Transactional
	@DisplayName("[성공] map api 아이디로 Crew 조회를 한다")
	void findByMapStoreId_success() {

		String mapStoreId = "16618597";

		User user = UserObjectProvider.createUser();

		userRepository.save(user);

		Store store = StoreObjectProvider.createStore(mapStoreId);

		storeRepository.save(store);

		List<Crew> crews = CrewObjectProvider.createCrews(user, store);

		crewRepository.saveAll(crews);

		CrewResponses crewResponses = crewService.findByMapStoreId(mapStoreId);

		List<CrewResponse> responses = crewResponses.responses();

		assertThat(responses).hasSize(crews.size());

	}

}