package com.prgrms.mukvengers.domain.review.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.prgrms.mukvengers.domain.review.model.Review;

public interface ReviewRepository extends JpaRepository<Review, Long> {

	@Query(value = """
		SELECT r
		FROM Review r
		WHERE r.reviewee.id = :userId
		""")
	Page<Review> findByReviewee(@Param(value = "userId") Long userId, Pageable pageable);

	@Query(value = """
		SELECT r
		FROM Review r
		WHERE r.reviewer.id = :userId
		""")
	Page<Review> findByReviewer(@Param(value = "userId") Long userId, Pageable pageable);

	@Query(value = """
		SELECT r
		FROM Review r
		WHERE r.crew.id = :crewId AND r.reviewee.id = :revieweeId AND r.reviewer.id = :reviewerId
		""")
	Optional<Review> findByReview(@Param(value = "crewId") Long crewId,
		@Param(value = "revieweeId") Long revieweeId, @Param(value = "reviewerId") Long reviewerId);
}
