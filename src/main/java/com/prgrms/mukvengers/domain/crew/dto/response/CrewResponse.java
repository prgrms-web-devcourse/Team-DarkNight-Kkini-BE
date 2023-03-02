package com.prgrms.mukvengers.domain.crew.dto.response;

import java.time.LocalDateTime;

import com.prgrms.mukvengers.domain.store.dto.response.StoreResponse;

public record CrewResponse(
	Long id,
	StoreResponse store,
	String name,
	String longitude,
	String latitude,
	Integer capacity,
	LocalDateTime promiseTime,
	String status,
	String content,
	String category
) {
}
