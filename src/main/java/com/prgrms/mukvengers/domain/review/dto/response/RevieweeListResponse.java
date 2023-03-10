package com.prgrms.mukvengers.domain.review.dto.response;

import com.prgrms.mukvengers.domain.crewmember.model.vo.CrewMemberRole;

public record RevieweeListResponse(
	Long userId,
	String nickname,
	String profileImgUrl,
	CrewMemberRole crewMemberRole,
	boolean isReviewed
) {

}