package com.prgrms.mukvengers.domain.store.service;

import static org.assertj.core.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Point;
import org.springframework.boot.test.context.SpringBootTest;

import com.prgrms.mukvengers.base.ServiceTest;
import com.prgrms.mukvengers.domain.store.dto.request.CreateStoreRequest;
import com.prgrms.mukvengers.domain.store.dto.response.StoreResponse;
import com.prgrms.mukvengers.domain.store.model.Store;
import com.prgrms.mukvengers.utils.StoreObjectProvider;

@SpringBootTest
class StoreServiceTest extends ServiceTest {

	@Test
	@DisplayName("[성공] Store 저장에 성공한다.")
	void create_success() {
		//given
		CreateStoreRequest createStoreRequest = StoreObjectProvider.getCreateStoreRequest("1234567899");

		double latitude = createStoreRequest.latitude();
		double longitude = createStoreRequest.longitude();
		Point location = gf.createPoint(new Coordinate(longitude, latitude));

		//when
		String mapStoreId = storeService.create(createStoreRequest);

		//then
		Optional<Store> optionalStore = storeRepository.findByMapStoreId(mapStoreId);

		assertThat(storeRepository.count()).isNotZero();
		assertThat(optionalStore).isPresent();
		Store store = optionalStore.get();
		assertThat(store)
			.hasFieldOrPropertyWithValue("location", location)
			.hasFieldOrPropertyWithValue("mapStoreId", createStoreRequest.mapStoreId());
	}

	@Test
	@DisplayName("[성공] 맵 api 아이디로 Store 조회를 성공한다.")
	void getByMapStoreId_success() {

		StoreResponse storeResponse = storeService.getByMapStoreId(savedStore.getMapStoreId());

		assertThat(storeResponse)
			.hasFieldOrPropertyWithValue("id", savedStore.getId())
			.hasFieldOrPropertyWithValue("longitude", savedStore.getLocation().getX())
			.hasFieldOrPropertyWithValue("latitude", savedStore.getLocation().getY())
			.hasFieldOrPropertyWithValue("mapStoreId", savedStore.getMapStoreId());
	}

}