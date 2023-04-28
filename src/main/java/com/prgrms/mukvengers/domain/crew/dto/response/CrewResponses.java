package com.prgrms.mukvengers.domain.crew.dto.response;

import java.util.List;

public record CrewResponses<T>(
	List<T> responses
) {
}
