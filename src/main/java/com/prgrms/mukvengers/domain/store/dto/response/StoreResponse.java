package com.prgrms.mukvengers.domain.store.dto.response;

public record StoreResponse(
	Long id,
	Double longitude,
	Double latitude,
	String mapStoreId,
	String placeName,
	String categories,
	String roadAddressName,
	Double rating,
	String photoUrls,
	String kakaoPlaceUrl,
	String phoneNumber
) {
}
