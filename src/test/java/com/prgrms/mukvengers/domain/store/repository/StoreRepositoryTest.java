package com.prgrms.mukvengers.domain.store.repository;

import static org.assertj.core.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.prgrms.mukvengers.base.RepositoryTest;
import com.prgrms.mukvengers.domain.store.model.Store;
import com.prgrms.mukvengers.utils.StoreObjectProvider;

class StoreRepositoryTest extends RepositoryTest {

	@Autowired
	private StoreRepository storeRepository;

	private Store savedStore;

	@BeforeEach
	void setUp() {
		savedStore = StoreObjectProvider.createStore();
		storeRepository.save(savedStore);
	}

	@Test
	@DisplayName("[성공] 맵 api로 받은 가게 아이디로 가게를 조회한다.")
	void findByApiId_success() {

		//when
		Optional<Store> optionalStore = storeRepository.findByMapStoreId(savedStore.getMapStoreId());

		//then
		assertThat(optionalStore).isPresent();
		Store store = optionalStore.get();

		assertThat(store)
			.usingRecursiveComparison()
			.isEqualTo(savedStore);
	}

}