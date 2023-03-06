package com.prgrms.mukvengers.domain.crew.repository;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

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

class CrewRepositoryTest extends RepositoryTest {

	@Test
	@DisplayName("[성공] 맵 api 아이디로 해당 가게의 밥 모임을 조회한다.")
	void joinStoreByPlaceId_success() {

		Integer page = 0;

		Integer size = 5;

		Pageable pageable = PageRequest.of(page, size);

		Slice<Crew> savedCrews = crewRepository.findAllByPlaceId(savedStore.getPlaceId(), pageable);

		assertThat(savedCrews).hasSize(size);
	}

	@Test
	@DisplayName("[성공] 현재 사용자의 위도와 경도, 그리고 거리를 받아 거리 안에 있는 밥 모임을 조회한다.")
	void findAllByDistance_success() {

		GeometryFactory gf = new GeometryFactory();
		double longitude = -147.4654321321;
		double latitude = 35.75413579;
		Point location = gf.createPoint(new Coordinate(longitude, latitude));
		List<Crew> savedCrews = crewRepository.findAllByLocation(location, 1000);

		assertThat(savedCrews).hasSize(crews.size());

	}
}