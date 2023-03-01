package com.prgrms.mukvengers.domain.review.api;

import java.net.URI;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.prgrms.mukvengers.domain.review.dto.request.CreateLeaderReviewRequest;
import com.prgrms.mukvengers.domain.review.dto.request.CreateMemberReviewRequest;
import com.prgrms.mukvengers.domain.review.dto.response.ReviewResponse;
import com.prgrms.mukvengers.domain.review.service.ReviewService;
import com.prgrms.mukvengers.global.common.dto.ApiResponse;
import com.prgrms.mukvengers.global.common.dto.IdResponse;
import com.prgrms.mukvengers.global.security.jwt.JwtAuthentication;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class ReviewController {

	private final ReviewService reviewService;

	/**
	 * 밥모임을 생성한 방장에 대한 리뷰를 생성한다.
	 */
	@PostMapping("/crews/{crewId}/reviews/leader")
	public ResponseEntity<IdResponse> createReviewOfLeader
	(
		@RequestBody @Valid CreateLeaderReviewRequest createLeaderReviewRequest,
		@AuthenticationPrincipal JwtAuthentication user,    // 리뷰를 작성하고자 하는 reviewer
		@PathVariable Long crewId
	) {
		IdResponse reviewIdResponse = reviewService.createReviewOfLeader(createLeaderReviewRequest, user.id(), crewId);
		URI location = UriComponentsBuilder.fromUriString("/api/v1/reviews/" + reviewIdResponse.id()).build().toUri();
		return ResponseEntity.created(location).build();
	}

	/**
	 * 방장을 제외한 밥모임에 참여한 밥모임원에 대한 리뷰를 생성한다.
	 */
	@PostMapping("/crews/{crewId}/reviews/member")
	public ResponseEntity<IdResponse> createReviewOfMember
	(
		@Valid @RequestBody CreateMemberReviewRequest createMemberReviewRequest,
		@AuthenticationPrincipal JwtAuthentication user,
		@PathVariable Long crewId
	) {
		IdResponse reviewIdResponse = reviewService.createMemberReview(createMemberReviewRequest, user.id(), crewId);
		URI location = UriComponentsBuilder.fromUriString("/api/v1/posts/" + reviewIdResponse.id()).build().toUri();
		return ResponseEntity.created(location).build();
	}

	/**
	 * 상세한 리뷰 조회를 위한 리뷰 단건 조회한다.
	 */
	@GetMapping("/reviews/{reviewId}")
	public ResponseEntity<ApiResponse<ReviewResponse>> getSingleReview
	(
		@PathVariable Long reviewId,
		@AuthenticationPrincipal JwtAuthentication user
	) {
		ReviewResponse findReview = reviewService.getSingleReview(reviewId, user.id());
		return ResponseEntity.ok().body(new ApiResponse<>(findReview));
	}
}
