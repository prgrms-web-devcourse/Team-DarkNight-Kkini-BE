package com.prgrms.mukvengers.domain.crew.service;

import static com.prgrms.mukvengers.domain.crew.model.vo.Category.*;
import static com.prgrms.mukvengers.domain.crew.model.vo.Status.*;
import static org.assertj.core.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.prgrms.mukvengers.base.ServiceTest;
import com.prgrms.mukvengers.domain.crew.dto.CreateCrewRequest;
import com.prgrms.mukvengers.domain.crew.model.Crew;
import com.prgrms.mukvengers.domain.crew.repository.CrewRepository;
import com.prgrms.mukvengers.domain.store.model.Store;
import com.prgrms.mukvengers.domain.store.repository.StoreRepository;
import com.prgrms.mukvengers.domain.user.model.User;
import com.prgrms.mukvengers.domain.user.repository.UserRepository;
import com.prgrms.mukvengers.global.common.dto.IdResponse;
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
	@DisplayName("[성공] Crew 저장에 성공한다.")
	@Transactional
	void create_success() {

		String mapStoreId = "16618597";
		String name = "원정대이름";
		String latitude = "35.75413579";
		String longitude = "-147.4654321321";
		Integer capacity = 5;
		String status = "모집중";
		String content = "저는 백엔드 개발자 입니다. 프론트 엔드 개발자 구해요";
		String category = "조용한";

		User user = UserObjectProvider.createUser();

		userRepository.save(user);

		Store store = StoreObjectProvider.createStore(mapStoreId);

		storeRepository.save(store);

		CreateCrewRequest createCrewRequest = new CreateCrewRequest(
			mapStoreId,
			name,
			latitude,
			longitude,
			capacity,
			status,
			content,
			category
		);

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
			.hasFieldOrPropertyWithValue("name", name)
			.hasFieldOrPropertyWithValue("location", location)
			.hasFieldOrPropertyWithValue("capacity", capacity)
			.hasFieldOrPropertyWithValue("status", RECRUITING)
			.hasFieldOrPropertyWithValue("content", content)
			.hasFieldOrPropertyWithValue("category", QUIET);
	}

}