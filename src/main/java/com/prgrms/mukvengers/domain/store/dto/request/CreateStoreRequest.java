package com.prgrms.mukvengers.domain.store.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public record CreateStoreRequest(
	@NotNull Double longitude,
	@NotNull Double latitude,
	@NotBlank String mapStoreId,
	@NotBlank String placeName,
	@NotBlank String categories,
	@NotBlank String roadAddressName,
	@NotNull Double rating,
	@NotBlank String photoUrls,
	@NotBlank String kakaoPlaceUrl,
	@NotBlank String phoneNumber
) {
}
