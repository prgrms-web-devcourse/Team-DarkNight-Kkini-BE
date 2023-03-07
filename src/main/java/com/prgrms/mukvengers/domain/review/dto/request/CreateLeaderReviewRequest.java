package com.prgrms.mukvengers.domain.review.dto.request;

import javax.validation.constraints.NotNull;

public record CreateLeaderReviewRequest(
	@NotNull Long leaderId,
	@NotNull String content,
	@NotNull Integer mannerScore,
	@NotNull Integer tasteScore
) {
}