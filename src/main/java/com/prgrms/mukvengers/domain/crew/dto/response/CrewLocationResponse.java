package com.prgrms.mukvengers.domain.crew.dto.response;

public record CrewLocationResponse(
	Double longitude,
	Double latitude,
	Long storeId,
	String placeName
) {
}
