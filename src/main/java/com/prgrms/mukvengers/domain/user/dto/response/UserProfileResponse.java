package com.prgrms.mukvengers.domain.user.dto.response;

public record UserProfileResponse(
	Long id,
	String nickname,
	String profileImgUrl,
	String introduction,
	Integer leaderCount,
	Integer crewCount,
	Integer tasteScore,
	String mannerScore
) {
}
