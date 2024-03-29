package com.prgrms.mukvengers.domain.review.repository;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.prgrms.mukvengers.base.RepositoryTest;
import com.prgrms.mukvengers.domain.crew.model.Crew;
import com.prgrms.mukvengers.domain.review.model.Review;
import com.prgrms.mukvengers.domain.user.model.User;
import com.prgrms.mukvengers.utils.CrewObjectProvider;
import com.prgrms.mukvengers.utils.ReviewObjectProvider;
import com.prgrms.mukvengers.utils.UserObjectProvider;

class ReviewRepositoryTest extends RepositoryTest {

	@Test
	@DisplayName("[성공] 리뷰이 아이디가 사용자 아이디와 같으면 나에 대한 리뷰를 조회할 수 있다.")
	void findByReviewee_success() {

		// given
		User createUser = UserObjectProvider.createUser("kakao_1234");
		User reviewer = userRepository.save(createUser);

		Crew createCrew = CrewObjectProvider.createCrew(savedStore);
		Crew crew = crewRepository.save(createCrew);

		Integer page = 0;

		Integer size = 5;

		Pageable pageable = PageRequest.of(page, size);

		Review review = ReviewObjectProvider.createLeaderReview(reviewer, savedUser1, crew);
		reviewRepository.save(review);

		// when
		Page<Review> findReview = reviewRepository.findByReviewee(savedUser1Id, pageable);

		// then
		assertThat(findReview.getContent().get(0).getReviewee().getId()).isEqualTo(savedUser1Id);
	}

	@Test
	@DisplayName("[성공] 리뷰어 아이디가 사용자 아이디와 같으면 내가 작성한 리뷰를 조회할 수 있다.")
	void findByReviewer_success() {

		// given
		User createUser = UserObjectProvider.createUser("kakao_1234");
		User reviewee = userRepository.save(createUser);

		Crew createCrew = CrewObjectProvider.createCrew(savedStore);
		Crew crew = crewRepository.save(createCrew);

		Integer page = 0;

		Integer size = 5;

		Pageable pageable = PageRequest.of(page, size);

		Review review = ReviewObjectProvider.createLeaderReview(savedUser1, reviewee, crew);
		reviewRepository.save(review);

		// when
		Page<Review> findReview = reviewRepository.findByReviewer(savedUser1Id, pageable);

		// then
		assertThat(findReview.getContent().get(0).getReviewer().getId()).isEqualTo(savedUser1Id);
	}

	@Test
	@DisplayName("[성공] 리뷰를 작성했는지 알 수 있다.")
	void existReview_success() {

		// given
		User createUser = UserObjectProvider.createUser("kakao_1");
		User reviewer = userRepository.save(createUser);

		Crew createCrew = CrewObjectProvider.createCrew(savedStore);
		Crew crew = crewRepository.save(createCrew);

		Review review = ReviewObjectProvider.createLeaderReview(reviewer, savedUser1, crew);
		reviewRepository.save(review);

		// when
		Optional<Review> reviewResult = reviewRepository.findByReview(crew.getId(),
			reviewer.getId(), savedUser1.getId());

		// then
		assertThat(reviewResult).isPresent();
	}
}