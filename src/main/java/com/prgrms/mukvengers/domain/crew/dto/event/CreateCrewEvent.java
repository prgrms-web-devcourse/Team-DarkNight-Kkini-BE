package com.prgrms.mukvengers.domain.crew.dto.event;

import com.prgrms.mukvengers.domain.crew.model.Crew;

public record CreateCrewEvent(
	Long userId,
	Crew crew
) {

}
