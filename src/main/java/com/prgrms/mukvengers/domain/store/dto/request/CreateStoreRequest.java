package com.prgrms.mukvengers.domain.store.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public record CreateStoreRequest(
	@NotNull(message = "올바르지 않는 경도입니다.") Double longitude,
	@NotNull(message = "올바르지 안흔 위도입니다.") Double latitude,
	@NotBlank(message = "올바르지 않는 api 아이디입니다.") String placeId,
	@NotBlank(message = "올바르지 않는 가게 이름입니다.") String placeName,
	@NotBlank(message = "올바르지 않는 카테고리입니다.") String categories,
	@NotBlank(message = "올바르지 않는 도로명 주소입니다.") String roadAddressName,
	String photoUrls,
	@NotBlank(message = "올바르지 않는 이미지 주소입니다.") String kakaoPlaceUrl,
	String phoneNumber
) {
}
