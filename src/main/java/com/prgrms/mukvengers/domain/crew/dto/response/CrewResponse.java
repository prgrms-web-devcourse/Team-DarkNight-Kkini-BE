package com.prgrms.mukvengers.domain.crew.dto.response;

import java.time.LocalDateTime;

import com.prgrms.mukvengers.domain.crew.model.vo.CrewStatus;

public record CrewResponse(
	Long id,
	String name,
	Integer currentMember,
	Integer capacity,
	CrewStatus status,
	String content,
	String category,
	LocalDateTime promiseTime
) {
}
