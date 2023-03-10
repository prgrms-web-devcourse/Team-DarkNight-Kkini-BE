package com.prgrms.mukvengers.domain.crew.dto.request;

import javax.validation.constraints.NotBlank;

import com.prgrms.mukvengers.domain.crew.model.vo.CrewStatus;

public record UpdateCrewStatusRequest(
	@NotBlank CrewStatus crewStatus
) {
}
