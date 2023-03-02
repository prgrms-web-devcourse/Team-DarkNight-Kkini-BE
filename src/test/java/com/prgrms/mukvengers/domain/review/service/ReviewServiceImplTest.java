package com.prgrms.mukvengers.domain.review.service;

import static com.prgrms.mukvengers.utils.CrewObjectProvider.*;
import static com.prgrms.mukvengers.utils.ReviewObjectProvider.*;
import static com.prgrms.mukvengers.utils.UserObjectProvider.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.prgrms.mukvengers.base.ServiceTest;
import com.prgrms.mukvengers.domain.crew.model.Crew;
import com.prgrms.mukvengers.domain.crewmember.model.CrewMember;
import com.prgrms.mukvengers.domain.crewmember.repository.CrewMemberRepository;
import com.prgrms.mukvengers.domain.review.dto.request.CreateLeaderReviewRequest;
import com.prgrms.mukvengers.domain.review.dto.request.CreateMemberReviewRequest;
import com.prgrms.mukvengers.domain.review.dto.response.ReviewResponse;
import com.prgrms.mukvengers.domain.review.model.Review;
import com.prgrms.mukvengers.domain.user.model.User;
import com.prgrms.mukvengers.global.common.dto.IdResponse;

class ReviewServiceImplTest extends ServiceTest {

	@Autowired
	CrewMemberRepository crewMemberRepository;

	User reviewer;
	User reviewee;

	@BeforeEach
	void setReview() {
		reviewer = savedUser;
		User user2 = User.builder()
			.nickname(DEFAULT_NICKNAME)
			.profileImgUrl(DEFAULT_PROFILE_IMG_URL)
			.provider(PROVIDER_KAKAO)
			.oauthId("asda")
			.build();
		reviewee = userRepository.save(user2);
	}

	@Test
	@Disabled
	@DisplayName("[성공] 리더에 대한 후기를 남길 경우 매너 온도와 맛잘알 평가를 할 수 있다.")
	void createLeaderReviewTest_success() {

		// given
		Crew crew = crewRepository.save(createCrew(savedStore));
		CrewMember createCrewMember = CrewMember.builder()
			.userId(reviewer.getId())
			.crew(crew)
			.build();

		CrewMember createMember = crewMemberRepository.save(createCrewMember);
		crew.addCrewMember(createMember);

		CreateLeaderReviewRequest leaderReviewRequest = createLeaderReviewRequest(reviewee.getId());

		// when
		reviewService.createReviewOfLeader(leaderReviewRequest, reviewer.getId(), crew.getId());

		// then
		assertThat(createMember.getUserId()).isEqualTo(reviewee.getId());
		assertThat(createMember.getCrew().getId()).isEqualTo(crew.getId());
	}

	@Test
	@Disabled
	@DisplayName("[성공] 밥모임원에 대한 후기를 남길 경우 매너 온도 평가를 할 수 있다.")
	void createMemberReviewTest_success() {
		// given
		User leader = userRepository.save(createUser());
		Crew crew = crewRepository.save(createCrew(savedStore));

		CrewMember createMemberOfReviewer = CrewMember.builder()
			.userId(reviewer.getId())
			.crew(crew)
			.build();

		CrewMember createMemberOfReviewee = CrewMember.builder()
			.userId(reviewee.getId())
			.crew(crew)
			.build();

		CrewMember crewMemberOfReviewer = crewMemberRepository.save(createMemberOfReviewer);
		CrewMember crewMemberOfReviewee = crewMemberRepository.save(createMemberOfReviewee);

		CreateMemberReviewRequest memberReviewRequest = createMemberReviewRequest(reviewee.getId());

		// when
		IdResponse review = reviewService.createMemberReview(memberReviewRequest,
			crewMemberOfReviewer.getUserId(), crew.getId());

		Review saveReview = reviewRepository.findById(review.id()).get();

		// then
		assertThat(saveReview.getMannerPoint()).isEqualTo(MANNER_POINT);
		assertThat(crewMemberOfReviewer.getCrew().getId()).isEqualTo(crewMemberOfReviewee.getCrew().getId());
	}

	@Test
	@DisplayName("[성공] reviewer가 본인이면 단건 리뷰 조회할 수 있다.")
	void getSingleLeaderReview() {
		// given
		Review review = reviewRepository.save(createLeaderReview(reviewer, reviewee, savedStore));

		// when
		ReviewResponse singleReview = reviewService.getSingleReview(review.getId(), reviewer.getId());

		// then
		assertThat(singleReview.reviewer().id()).isEqualTo(reviewer.getId());
	}
}