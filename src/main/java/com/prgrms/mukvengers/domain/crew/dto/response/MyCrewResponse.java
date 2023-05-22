package com.prgrms.mukvengers.domain.crew.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import com.prgrms.mukvengers.domain.crew.model.vo.CrewStatus;
import com.prgrms.mukvengers.domain.crewmember.dto.response.MyCrewMemberResponse;

public record MyCrewResponse(
	Long id,
	String placeName,
	String name,
	Integer currentMember,
	Integer capacity,
	CrewStatus crewStatus,
	String content,
	String category,
	LocalDateTime promiseTime,
	List<MyCrewMemberResponse> members
) {
}
