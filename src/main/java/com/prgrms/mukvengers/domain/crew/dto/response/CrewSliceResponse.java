package com.prgrms.mukvengers.domain.crew.dto.response;

import org.springframework.data.domain.Slice;

public record CrewSliceResponse(
	Slice<CrewResponse> responses
) {
}
