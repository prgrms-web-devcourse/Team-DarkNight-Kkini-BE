package com.prgrms.mukvengers.domain.crew.dto.response;

import java.util.List;

public record MyCrewResponse(
	List<CrewResponse> responses,
	String profileImgUrl
) {
}
