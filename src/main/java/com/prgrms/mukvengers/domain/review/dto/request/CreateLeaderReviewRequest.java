package com.prgrms.mukvengers.domain.review.dto.request;

import javax.validation.constraints.NotNull;

public record CreateLeaderReviewRequest(
	@NotNull Long leaderId, // reviewee : 방장
	@NotNull String content,
	@NotNull Integer mannerPoint,
	@NotNull Integer tastePoint
) {
}