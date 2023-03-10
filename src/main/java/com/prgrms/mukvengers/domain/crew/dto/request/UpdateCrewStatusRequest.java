package com.prgrms.mukvengers.domain.crew.dto.request;

import javax.validation.constraints.NotBlank;

public record UpdateCrewStatusRequest(
	@NotBlank String crewStatus
) {
}
