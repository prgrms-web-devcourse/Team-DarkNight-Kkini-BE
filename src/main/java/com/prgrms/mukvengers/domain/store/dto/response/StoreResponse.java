package com.prgrms.mukvengers.domain.store.dto.response;

public record StoreResponse(
	Long id,
	Double longitude,
	Double latitude,
	String placeId,
	String placeName,
	String categories,
	String roadAddressName,
	String photoUrls,
	String kakaoPlaceUrl,
	String phoneNumber
) {
}
