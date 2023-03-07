package com.prgrms.mukvengers.domain.crewmember.dto.response;

import com.prgrms.mukvengers.domain.crewmember.model.vo.Role;

public record CrewMemberResponse(
	Long userId,
	String nickname,
	String profileImgUrl,
	Role role
) {
}
