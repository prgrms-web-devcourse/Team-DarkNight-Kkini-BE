package com.prgrms.mukvengers.domain.crew.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import com.prgrms.mukvengers.domain.crew.model.vo.CrewStatus;
import com.prgrms.mukvengers.domain.crewmember.dto.response.CrewMemberResponse;
import com.prgrms.mukvengers.domain.proposal.model.vo.ProposalStatus;
import com.prgrms.mukvengers.domain.store.dto.response.StoreResponse;

public record CrewDetailResponse(
	Long id,
	StoreResponse response,
	String name,
	Integer currentMember,
	Integer capacity,
	CrewStatus crewStatus,
	String content,
	String category,
	LocalDateTime promiseTime,
	List<CrewMemberResponse> members,
	ProposalStatus proposalStatus
) {
}
