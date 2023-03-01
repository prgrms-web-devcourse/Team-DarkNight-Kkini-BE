package com.prgrms.mukvengers.utils;

import java.time.LocalDateTime;

import com.prgrms.mukvengers.domain.review.dto.request.CreateLeaderReviewRequest;
import com.prgrms.mukvengers.domain.review.dto.request.CreateMemberReviewRequest;
import com.prgrms.mukvengers.domain.review.model.Review;
import com.prgrms.mukvengers.domain.store.model.Store;
import com.prgrms.mukvengers.domain.user.model.User;

public class ReviewObjectProvider {

	private static final LocalDateTime PROMISE_TIME = LocalDateTime.now();
	private static final String CREW_NAME = "밥모임 이름";
	private static final String CONTENT = "추가로 작성하고 싶은 내용을 입력해주세요.";
	public static final Integer MANNER_POINT = 10;
	private static final Integer TASTE_POINT = 5;

	public static Review createLeaderReview(User reviewer, User reviewee, Store store) {
		return Review.builder()
			.reviewer(reviewer)
			.reviewee(reviewee)
			.store(store)
			.promiseTime(PROMISE_TIME)
			.crewName(CREW_NAME)
			.content(CONTENT)
			.mannerPoint(MANNER_POINT)
			.tastePoint(TASTE_POINT)
			.build();
	}

	public static Review createMemberReview(User reviewer, User reviewee, Store store) {
		return Review.builder()
			.reviewer(reviewer)
			.reviewee(reviewee)
			.store(store)
			.promiseTime(PROMISE_TIME)
			.crewName(CREW_NAME)
			.content(CONTENT)
			.mannerPoint(MANNER_POINT)
			.build();
	}

	public static CreateLeaderReviewRequest createLeaderReviewRequest(Long revieweeId) {
		return new CreateLeaderReviewRequest(
			revieweeId,
			CONTENT,
			MANNER_POINT,
			TASTE_POINT
		);
	}

	public static CreateMemberReviewRequest createMemberReviewRequest(Long revieweeId) {
		return new CreateMemberReviewRequest(
			revieweeId,
			CONTENT,
			MANNER_POINT
		);
	}
}