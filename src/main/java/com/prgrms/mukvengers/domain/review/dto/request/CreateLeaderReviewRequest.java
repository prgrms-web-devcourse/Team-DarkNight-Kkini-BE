package com.prgrms.mukvengers.domain.review.dto.request;

import javax.validation.constraints.NotNull;

import org.springframework.lang.Nullable;

public record CreateLeaderReviewRequest(@NotNull Long leaderId, // reviewee : 방장
										@Nullable String content,
										@NotNull Integer mannerPoint,
										@NotNull Integer tastePoint) {
}