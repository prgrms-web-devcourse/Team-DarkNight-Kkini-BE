package com.prgrms.mukvengers.domain.crew.dto.request;

import javax.validation.constraints.NotNull;

public record SearchCrewRequest(
	@NotNull Double longitude,
	@NotNull Double latitude,
	@NotNull Integer distance
) {
}
