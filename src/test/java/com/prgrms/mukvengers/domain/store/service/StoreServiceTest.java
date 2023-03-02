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
import com.prgrms.mukvengers.domain.store.model.Store;
import com.prgrms.mukvengers.utils.StoreObjectProvider;

@SpringBootTest
class StoreServiceTest extends ServiceTest {

	@Test
	@DisplayName("[성공] Store 저장에 성공한다.")
	void create_success() {
		//given
		CreateStoreRequest createStoreRequest = StoreObjectProvider.getCreateStoreRequest("1234567899");

		double latitude = Double.parseDouble(createStoreRequest.latitude());
		double longitude = Double.parseDouble(createStoreRequest.longitude());
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

}