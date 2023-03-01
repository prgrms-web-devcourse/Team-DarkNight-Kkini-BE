package com.prgrms.mukvengers.domain.review.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prgrms.mukvengers.domain.review.model.Review;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}
