package com.prgrms.mukvengers.domain.review.dto.request;

import javax.validation.constraints.NotNull;

import org.springframework.lang.Nullable;

public record CreateMemberReviewRequest(@NotNull Long revieweeId,
										@Nullable String content,
										@NotNull Integer mannerPoint) {
}