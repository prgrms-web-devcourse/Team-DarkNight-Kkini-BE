package com.prgrms.mukvengers.domain.crew.dto.request;

import javax.validation.constraints.NotNull;

public record UpdateStatusRequest(
	@NotNull Long crewId
) {
}
