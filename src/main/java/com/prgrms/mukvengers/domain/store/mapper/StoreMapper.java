package com.prgrms.mukvengers.domain.store.mapper;

import org.mapstruct.Mapper;

import com.prgrms.mukvengers.domain.store.dto.request.CreateStoreRequest;
import com.prgrms.mukvengers.domain.store.model.Store;

@Mapper(componentModel = "spring")
public interface StoreMapper {

	Store toStore(CreateStoreRequest createStoreRequest);

}
