package com.prgrms.mukvengers.domain.store.service;

import com.prgrms.mukvengers.domain.store.dto.request.CreateStoreRequest;
import com.prgrms.mukvengers.domain.store.dto.response.StoreResponse;

public interface StoreService {

	String create(CreateStoreRequest createStoreRequest);

	StoreResponse getByMapStoreId(String mapStoreId);
}
