package com.prgrms.mukvengers.domain.store.dto.response;

public record StoreResponse(
	Long id,
	String longitude,
	String latitude,
	String mapStoreId
) {
}
