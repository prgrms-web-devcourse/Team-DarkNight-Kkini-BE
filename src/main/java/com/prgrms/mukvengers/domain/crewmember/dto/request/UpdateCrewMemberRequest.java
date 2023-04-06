package com.prgrms.mukvengers.domain.crewmember.dto.request;

import javax.validation.constraints.NotNull;

public record UpdateCrewMemberRequest(
	@NotNull Long blockUserId
) {
}
