package com.prgrms.mukvengers.domain.crew.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import com.prgrms.mukvengers.domain.crewmember.dto.response.CrewMemberResponse;
import com.prgrms.mukvengers.domain.store.dto.response.StoreResponse;

public record CrewDetailResponse(
	Long id,
	StoreResponse response,
	String name,
	Integer currentMember,
	Integer capacity,
	String status,
	String content,
	String category,
	LocalDateTime promiseTime,
	List<CrewMemberResponse> members
) {
}
