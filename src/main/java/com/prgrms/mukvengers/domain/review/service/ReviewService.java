package com.prgrms.mukvengers.domain.review.service;

import com.prgrms.mukvengers.domain.review.dto.request.CreateLeaderReviewRequest;
import com.prgrms.mukvengers.domain.review.dto.request.CreateMemberReviewRequest;
import com.prgrms.mukvengers.domain.review.dto.response.ReviewResponse;
import com.prgrms.mukvengers.global.common.dto.IdResponse;

public interface ReviewService {
	IdResponse createReviewOfLeader(CreateLeaderReviewRequest request, Long reviewerId, Long crewId);

	IdResponse createMemberReview(CreateMemberReviewRequest request, Long reviewerId, Long crewId);

	ReviewResponse getSingleReview(Long reviewId, Long userId);
}
