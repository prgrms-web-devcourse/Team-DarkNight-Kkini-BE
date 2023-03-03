package com.prgrms.mukvengers.domain.review.dto.request;

import javax.validation.constraints.NotNull;

public record CreateMemberReviewRequest(
	@NotNull Long revieweeId,
	@NotNull String content,
	@NotNull Integer mannerPoint
) {
}