package com.prgrms.mukvengers.domain.store.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prgrms.mukvengers.domain.store.dto.request.CreateStoreRequest;
import com.prgrms.mukvengers.domain.store.mapper.StoreMapper;
import com.prgrms.mukvengers.domain.store.model.Store;
import com.prgrms.mukvengers.domain.store.repository.StoreRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StoreService {

	private final StoreRepository storeRepository;
	private final StoreMapper storeMapper;

	@Transactional
	public String create(CreateStoreRequest createStoreRequest) {

		Store store = storeMapper.toStore(createStoreRequest);

		Store save = storeRepository.save(store);

		return save.getMapStoreId();
	}

}
