package com.prgrms.mukvengers.domain.crewmember.dto.response;

import com.prgrms.mukvengers.domain.crewmember.model.vo.CrewMemberRole;

public record CrewMemberResponse(
	Long userId,
	String nickname,
	String profileImgUrl,
	CrewMemberRole crewMemberRole
) {
}
