package com.prgrms.mukvengers.domain.review.api;

import static org.springframework.http.MediaType.*;

import java.net.URI;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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
	 * <pre>
	 *     밥모임을 생성한 방장에 대한 리뷰를 생성한다.
	 * </pre>
	 * @param createLeaderReviewRequest 방장 리뷰 생성 DTO
	 * @param user 리뷰 작성자
	 * @param crewId 리뷰 작성자가 참여한 모임
	 * @return status :201, body : 생성된 리뷰 조회 redirectUri
	 */
	@PostMapping(value = "/crews/{crewId}/reviews/leader", consumes = APPLICATION_JSON_VALUE)
	public ResponseEntity<IdResponse> createLeaderReview
	(
		@PathVariable Long crewId,
		@RequestBody @Valid CreateLeaderReviewRequest createLeaderReviewRequest,
		@AuthenticationPrincipal JwtAuthentication user    // 리뷰를 작성하고자 하는 reviewer
	) {
		IdResponse reviewIdResponse = reviewService.createLeaderReview(createLeaderReviewRequest, user.id(), crewId);
		URI location = UriComponentsBuilder.fromUriString("/api/v1/reviews/" + reviewIdResponse.id()).build().toUri();
		return ResponseEntity.created(location).build();
	}

	/**
	 * <pre>
	 *     방장을 제외한 밥모임에 참여한 밥모임원에 대한 리뷰를 생성
	 * </pre>
	 * @param createMemberReviewRequest 일반 리뷰 생성 DTO
	 * @param user 리뷰 작성자
	 * @param crewId 리뷰 작성자가 참여한 모임
	 * @return status :201, body : 생성된 리뷰 조회 redirectUri
	 */
	@PostMapping(value = "/crews/{crewId}/reviews/member", consumes = APPLICATION_JSON_VALUE)
	public ResponseEntity<IdResponse> createMemberReview
	(
		@PathVariable Long crewId,
		@RequestBody @Valid CreateMemberReviewRequest createMemberReviewRequest,
		@AuthenticationPrincipal JwtAuthentication user
	) {
		IdResponse reviewIdResponse = reviewService.createMemberReview(createMemberReviewRequest, user.id(), crewId);
		URI location = UriComponentsBuilder.fromUriString("/api/v1/reviews/" + reviewIdResponse.id()).build().toUri();
		return ResponseEntity.created(location).build();
	}

	/**
	 * <pre>
	 *     자신의 작성한 리뷰이거나, 자신에게 작성된 리뷰 단건 조회
	 * </pre>
	 * @param reviewId 조회할 리뷰 아이디
	 * @param user 사용자 정보
	 * @return status : 200 body : 조회된 리뷰 데이터
	 */
	@GetMapping(value = "/reviews/{reviewId}", produces = APPLICATION_JSON_VALUE)
	public ResponseEntity<ApiResponse<ReviewResponse>> getSingleReview
	(
		@PathVariable Long reviewId,
		@AuthenticationPrincipal JwtAuthentication user
	) {
		ReviewResponse findReview = reviewService.getSingleReview(reviewId, user.id());
		return ResponseEntity.ok().body(new ApiResponse<>(findReview));
	}

	/**
	 * <pre>
	 *     자신에게 작성된 리뷰 전체 조회
	 * </pre>
	 * @param user 사용자 정보
	 * @param pageable 페이징 데이터
	 * @return status : 200 body : 자신에게 작성된 리뷰 전체 데이터
	 */
	@GetMapping(value = "/reviews/me", produces = APPLICATION_JSON_VALUE)
	public ResponseEntity<ApiResponse<Page<ReviewResponse>>> getAllReceivedReview
		(
			@AuthenticationPrincipal JwtAuthentication user,
			@PageableDefault(sort = "updatedAt", direction = Sort.Direction.DESC) Pageable pageable
		) {
		Page<ReviewResponse> reviews = reviewService.getAllReceivedReview(user.id(), pageable);
		return ResponseEntity.ok().body(new ApiResponse<>(reviews));
	}

	/**
	 * <pre>
	 *     자신이 작성한 리뷰 전체 조회
	 * </pre>
	 * @param user 사용자 정보
	 * @param pageable 페이징 데이터
	 * @return status : 200 body : 자신이 작성한 리뷰 전체 데이터
	 */
	@GetMapping(value = "/reviews", produces = APPLICATION_JSON_VALUE)
	public ResponseEntity<ApiResponse<Page<ReviewResponse>>> getAllWroteReview
	(
		@AuthenticationPrincipal JwtAuthentication user,
		@PageableDefault(sort = "updatedAt", direction = Sort.Direction.DESC) Pageable pageable
	) {
		Page<ReviewResponse> reviews = reviewService.getAllWroteReview(user.id(), pageable);
		return ResponseEntity.ok().body(new ApiResponse<>(reviews));
	}
}
