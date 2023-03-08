package com.prgrms.mukvengers.domain.review.dto.request;

import javax.validation.constraints.NotNull;

import org.springframework.lang.Nullable;

public record CreateLeaderReviewRequest(
	@NotNull Long leaderId,
	@Nullable String content,
	@NotNull Integer mannerScore,
	@NotNull Integer tasteScore
) {
}