package com.prgrms.mukvengers.domain.crew.dto.response;

import java.time.LocalDateTime;

public record CrewResponse(
	Long id,
	String name,
	Integer currentMember,
	Integer capacity,
	String status,
	String content,
	String category,
	LocalDateTime promiseTime
) {
}
