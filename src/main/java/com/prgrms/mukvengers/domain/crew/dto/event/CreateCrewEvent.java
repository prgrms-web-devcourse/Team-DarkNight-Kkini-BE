package com.prgrms.mukvengers.domain.crew.dto.event;

public record CreateCrewEvent(
	Long userId,
	Long crewId
) {

}
