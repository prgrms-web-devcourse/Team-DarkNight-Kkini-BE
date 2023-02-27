package com.prgrms.mukvengers.domain.store.dto.response;

public record StoreResponse(
	Long id,
	String latitude,
	String longitude,
	String mapStoreId
) {
}
