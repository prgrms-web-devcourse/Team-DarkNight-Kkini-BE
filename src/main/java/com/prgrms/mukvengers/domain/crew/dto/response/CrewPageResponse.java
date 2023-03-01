package com.prgrms.mukvengers.domain.crew.dto.response;

import org.springframework.data.domain.Page;

public record CrewPageResponse(
	Page<CrewResponse> responses
) {
}
