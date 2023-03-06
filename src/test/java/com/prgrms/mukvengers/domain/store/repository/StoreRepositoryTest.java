package com.prgrms.mukvengers.domain.store.repository;

import static org.assertj.core.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.prgrms.mukvengers.base.RepositoryTest;
import com.prgrms.mukvengers.domain.store.model.Store;

class StoreRepositoryTest extends RepositoryTest {

	@Test
	@DisplayName("[성공] 맵 api로 받은 가게 아이디로 가게를 조회한다.")
	void findByApiId_success() {

		//when
		Optional<Store> optionalStore = storeRepository.findByPlaceId(savedStore.getPlaceId());

		//then
		assertThat(optionalStore).isPresent();
		Store store = optionalStore.get();

		assertThat(store)
			.usingRecursiveComparison()
			.isEqualTo(savedStore);
	}

}