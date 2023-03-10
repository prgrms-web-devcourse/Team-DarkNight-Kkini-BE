package com.prgrms.mukvengers.domain.review.service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prgrms.mukvengers.domain.crew.exception.CrewNotFoundException;
import com.prgrms.mukvengers.domain.crew.model.Crew;
import com.prgrms.mukvengers.domain.crew.repository.CrewRepository;
import com.prgrms.mukvengers.domain.crewmember.exception.LeaderNotFoundException;
import com.prgrms.mukvengers.domain.crewmember.exception.MemberNotFoundException;
import com.prgrms.mukvengers.domain.crewmember.model.CrewMember;
import com.prgrms.mukvengers.domain.crewmember.model.vo.CrewMemberRole;
import com.prgrms.mukvengers.domain.crewmember.repository.CrewMemberRepository;
import com.prgrms.mukvengers.domain.review.dto.request.CreateLeaderReviewRequest;
import com.prgrms.mukvengers.domain.review.dto.request.CreateMemberReviewRequest;
import com.prgrms.mukvengers.domain.review.dto.response.ReviewResponse;
import com.prgrms.mukvengers.domain.review.dto.response.RevieweeListResponse;
import com.prgrms.mukvengers.domain.review.exception.ReviewNotAccessException;
import com.prgrms.mukvengers.domain.review.exception.ReviewNotFoundException;
import com.prgrms.mukvengers.domain.review.mapper.ReviewMapper;
import com.prgrms.mukvengers.domain.review.model.Review;
import com.prgrms.mukvengers.domain.review.repository.ReviewRepository;
import com.prgrms.mukvengers.domain.user.exception.UserNotFoundException;
import com.prgrms.mukvengers.domain.user.model.User;
import com.prgrms.mukvengers.domain.user.repository.UserRepository;
import com.prgrms.mukvengers.global.common.dto.IdResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewServiceImpl implements ReviewService {

	private final UserRepository userRepository;
	private final CrewRepository crewRepository;
	private final CrewMemberRepository crewMemberRepository;
	private final ReviewRepository reviewRepository;
	private final ReviewMapper reviewMapper;

	@Override
	@Transactional
	public IdResponse createLeaderReview(CreateLeaderReviewRequest leaderReviewRequest,
		Long reviewerId, Long crewId) {

		User reviewer = getUserByUserId(reviewerId);
		User reviewee = getUserByUserId(leaderReviewRequest.leaderId());

		Crew crew = crewRepository.findById(crewId)
			.orElseThrow(() -> new CrewNotFoundException(crewId));

		crewMemberRepository.findCrewMemberByCrewIdAndUserId(crewId, reviewee.getId())
			.filter(crewMember -> crewMember.getCrewMemberRole() == CrewMemberRole.LEADER)
			.orElseThrow(() -> new LeaderNotFoundException(reviewee.getId()));

		crewMemberRepository.findCrewMemberByCrewIdAndUserId(crewId, reviewer.getId())
			.filter(crewMember -> crewMember.getCrewMemberRole() == CrewMemberRole.MEMBER)
			.orElseThrow(() -> new MemberNotFoundException(reviewer.getId()));

		Review review = reviewMapper.toReview(leaderReviewRequest, reviewer, reviewee, crew);

		Review saveReview = reviewRepository.save(review);

		return new IdResponse(saveReview.getId());
	}

	@Override
	@Transactional
	public IdResponse createMemberReview(CreateMemberReviewRequest memberReviewRequest, Long reviewerId, Long crewId) {

		User reviewer = getUserByUserId(reviewerId);
		User reviewee = getUserByUserId(memberReviewRequest.revieweeId());

		Crew crew = crewRepository.findById(crewId)
			.orElseThrow(() -> new CrewNotFoundException(crewId));

		crewMemberRepository.findCrewMemberByCrewIdAndUserId(crewId, reviewee.getId())
			.filter(crewMember -> crewMember.getCrewMemberRole() == CrewMemberRole.MEMBER)
			.orElseThrow(() -> new MemberNotFoundException(reviewee.getId()));

		crewMemberRepository.findCrewMemberByCrewIdAndUserId(crewId, reviewer.getId())
			.filter(crewMember -> crewMember.getCrewMemberRole() == CrewMemberRole.MEMBER)
			.orElseThrow(() -> new MemberNotFoundException(reviewee.getId()));

		Review review = reviewMapper.toReview(memberReviewRequest, reviewer, reviewee, crew);

		Review saveReview = reviewRepository.save(review);

		reviewee.addMannerScore(saveReview.getMannerScore());

		return new IdResponse(saveReview.getId());
	}

	@Override
	public ReviewResponse getSingleReview(Long reviewId, Long userId) {

		Review review = reviewRepository.findById(reviewId)
			.orElseThrow(() -> new ReviewNotFoundException(reviewId));

		if (!review.getReviewer().isSameUser(userId) && !review.getReviewee().isSameUser(userId)) {
			throw new ReviewNotAccessException(userId);
		}

		return reviewMapper.toReviewResponse(review);
	}

	@Override
	public Page<ReviewResponse> getAllReceivedReview(Long userId, Pageable pageable) {

		return reviewRepository.findByReviewee(userId, pageable)
			.map(reviewMapper::toReviewResponse);
	}

	@Override
	public Page<ReviewResponse> getAllWroteReview(Long userId, Pageable pageable) {

		return reviewRepository.findByReviewer(userId, pageable)
			.map(reviewMapper::toReviewResponse);
	}

	@Override
	public List<RevieweeListResponse> getRevieweeListFromCrew(Long userId, Long crewId) {
		User reviewer = getUserByUserId(userId);

		return crewMemberRepository.findAllByCrewId(crewId)
			.stream()
			.filter(crewMember -> !Objects.equals(crewMember.getUserId(), reviewer.getId()))
			.map(crewMember -> toRevieweeResponse(crewId, reviewer, crewMember))
			.toList();
	}

	private RevieweeListResponse toRevieweeResponse(Long crewId, User reviewer, CrewMember crewMember) {
		User reviewee = getUserByUserId(crewMember.getUserId());
		Optional<Review> review = reviewRepository.findByReview(crewId, reviewer.getId(), reviewee.getId());
		boolean isReviewed = review.isPresent();
		return reviewMapper.toRevieweeListResponse(reviewee, crewMember.getCrewMemberRole(), isReviewed);
	}

	private User getUserByUserId(Long userId) {
		return userRepository.findById(userId)
			.orElseThrow(() -> new UserNotFoundException(userId));
	}
}
