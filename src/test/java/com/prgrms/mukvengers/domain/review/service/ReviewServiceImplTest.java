package com.prgrms.mukvengers.domain.review.service;

import static com.prgrms.mukvengers.utils.CrewObjectProvider.*;
import static com.prgrms.mukvengers.utils.ReviewObjectProvider.*;
import static com.prgrms.mukvengers.utils.UserObjectProvider.*;
import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.prgrms.mukvengers.base.ServiceTest;
import com.prgrms.mukvengers.domain.crew.model.Crew;
import com.prgrms.mukvengers.domain.crewmember.model.CrewMember;
import com.prgrms.mukvengers.domain.crewmember.model.vo.CrewMemberRole;
import com.prgrms.mukvengers.domain.review.dto.request.CreateLeaderReviewRequest;
import com.prgrms.mukvengers.domain.review.dto.request.CreateMemberReviewRequest;
import com.prgrms.mukvengers.domain.review.dto.response.ReviewResponse;
import com.prgrms.mukvengers.domain.review.dto.response.RevieweeListResponse;
import com.prgrms.mukvengers.domain.review.model.Review;
import com.prgrms.mukvengers.domain.user.model.User;
import com.prgrms.mukvengers.global.common.dto.IdResponse;
import com.prgrms.mukvengers.utils.CrewMemberObjectProvider;

class ReviewServiceImplTest extends ServiceTest {

	private User reviewer;
	private User reviewee;
	private Crew crew;

	@BeforeEach
	void setReview() {
		reviewer = savedUser;
		reviewee = userRepository.save(createUser("kakao_1212"));
		crew = crewRepository.save(createCrew(savedStore));
	}

	@Test
	@DisplayName("[성공] 리더에 대한 후기를 남길 경우 매너 온도와 맛잘알 평가를 할 수 있다.")
	void createLeaderReviewTest_success() {
		// given
		CrewMember createMemberOfLeader = CrewMemberObjectProvider.createCrewMember(reviewee.getId(), crew,
			CrewMemberRole.LEADER);
		CrewMember createMemberOfMember = CrewMemberObjectProvider.createCrewMember(reviewer.getId(), crew,
			CrewMemberRole.MEMBER);

		CrewMember leader = crewMemberRepository.save(createMemberOfLeader);
		CrewMember member = crewMemberRepository.save(createMemberOfMember);

		crew.addCrewMember(member);
		crew.addCrewMember(leader);

		CreateLeaderReviewRequest leaderReviewRequest = createLeaderReviewRequest(reviewee.getId());

		// when
		IdResponse leaderReview = reviewService.createLeaderReview(leaderReviewRequest, reviewer.getId(), crew.getId());
		Optional<Review> findReview = reviewRepository.findById(leaderReview.id());

		// then
		assertThat(findReview).isPresent();
		assertThat(findReview.get().getReviewee().getId()).isEqualTo(leader.getUserId());
	}

	@Test
	@DisplayName("[성공] 밥모임원에 대한 후기를 남길 경우 매너 온도 평가를 할 수 있다.")
	void createMemberReviewTest_success() {
		// given
		CrewMember createMemberOfReviewer = CrewMemberObjectProvider.createCrewMember(reviewer.getId(), crew,
			CrewMemberRole.MEMBER);
		CrewMember createMemberOfReviewee = CrewMemberObjectProvider.createCrewMember(reviewee.getId(), crew,
			CrewMemberRole.MEMBER);

		CrewMember crewMemberOfReviewer = crewMemberRepository.save(createMemberOfReviewer);
		CrewMember crewMemberOfReviewee = crewMemberRepository.save(createMemberOfReviewee);

		crew.addCrewMember(crewMemberOfReviewer);
		crew.addCrewMember(crewMemberOfReviewee);

		CreateMemberReviewRequest memberReviewRequest = createMemberReviewRequest(reviewee.getId());

		// when
		IdResponse review = reviewService.createMemberReview(memberReviewRequest, reviewer.getId(), crew.getId());
		Optional<Review> findReview = reviewRepository.findById(review.id());

		// then
		assertThat(findReview).isPresent();
		assertThat(findReview.get().getMannerScore()).isEqualTo(MANNER_SCORE);
		assertThat(findReview.get().getTasteScore()).isZero();
	}

	@Test
	@DisplayName("[성공] reviewer가 본인이면 단건 리뷰 조회할 수 있다.")
	void getSingleLeaderReview_success() {
		// given
		Review review = reviewRepository.save(createLeaderReview(reviewer, reviewee, crew));

		// when
		ReviewResponse singleReview = reviewService.getSingleReview(review.getId(), reviewer.getId());

		// then
		assertThat(singleReview.reviewer().id()).isEqualTo(reviewer.getId());
	}

	@Test
	@DisplayName("[성공] 리뷰이 아이디가 사용자 아이디와 같으면 본인에 대한 리뷰를 조회할 수 있다.")
	void getAllReceivedReview_success() {
		// given
		Review review = createLeaderReview(reviewer, reviewee, crew);
		reviewRepository.save(review);

		Pageable pageable = PageRequest.of(0, 10);

		// when
		Page<ReviewResponse> reviews = reviewService.getAllReceivedReview(reviewee.getId(), pageable);

		assertThat(reviews).hasSize(1);
		assertThat(reviews.getContent().get(0).reviewee().id()).isEqualTo(reviewee.getId());
	}

	@Test
	@DisplayName("[성공] 리뷰어 아이디가 사용자 아이디와 같으면 본인이 작성한 리뷰를 조회할 수 있다.")
	void getAllWroteReview_success() {
		// given
		Review review = createMemberReview(reviewer, reviewee, crew);
		reviewRepository.save(review);

		Pageable pageable = PageRequest.of(0, 10);

		// when
		Page<ReviewResponse> reviews = reviewService.getAllWroteReview(reviewer.getId(), pageable);

		assertThat(reviews).hasSize(1);
		assertThat(reviews.getContent().get(0).reviewer().id()).isEqualTo(reviewer.getId());
	}

	@Test
	@DisplayName("[성공] 내가 리뷰를 남긴 사용자와 아닌 사용자를 조회할 수 있다.")
	void getRevieweeListFromCrew_success() {
		// given
		CrewMember createMemberOfReviewer = CrewMemberObjectProvider.createCrewMember(reviewer.getId(), crew,
			CrewMemberRole.MEMBER);
		CrewMember createMemberOfReviewee = CrewMemberObjectProvider.createCrewMember(reviewee.getId(), crew,
			CrewMemberRole.MEMBER);

		CrewMember crewMemberOfReviewer = crewMemberRepository.save(createMemberOfReviewer);
		CrewMember crewMemberOfReviewee = crewMemberRepository.save(createMemberOfReviewee);

		crew.addCrewMember(crewMemberOfReviewer);
		crew.addCrewMember(crewMemberOfReviewee);

		Review review = createMemberReview(reviewer, reviewee, crew);
		reviewRepository.save(review);

		// when
		List<RevieweeListResponse> myReviewee
			= reviewService.getRevieweeListFromCrew(reviewer.getId(), crew.getId());
		// then
		assertThat(myReviewee).isNotEmpty();
		assertThat(myReviewee.get(0).userId()).isEqualTo(reviewee.getId());
		assertThat(myReviewee.get(0).isReviewed()).isTrue();
	}

}