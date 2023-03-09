package com.prgrms.mukvengers.domain.review.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.prgrms.mukvengers.domain.crew.model.Crew;
import com.prgrms.mukvengers.domain.crewmember.model.vo.CrewMemberRole;
import com.prgrms.mukvengers.domain.review.dto.request.CreateLeaderReviewRequest;
import com.prgrms.mukvengers.domain.review.dto.request.CreateMemberReviewRequest;
import com.prgrms.mukvengers.domain.review.dto.response.ReviewResponse;
import com.prgrms.mukvengers.domain.review.dto.response.RevieweeListResponse;
import com.prgrms.mukvengers.domain.review.model.Review;
import com.prgrms.mukvengers.domain.user.model.User;

@Mapper(componentModel = "spring")
public interface ReviewMapper {

	@Mapping(source = "reviewer", target = "reviewer")
	@Mapping(source = "reviewee", target = "reviewee")
	@Mapping(source = "crew", target = "crew")
	@Mapping(source = "crew.promiseTime", target = "promiseTime")
	@Mapping(source = "request.content", target = "content")
	@Mapping(source = "request.mannerScore", target = "mannerScore")
	@Mapping(source = "request.tasteScore", target = "tasteScore")
	Review toReview(CreateLeaderReviewRequest request, User reviewer, User reviewee, Crew crew);

	@Mapping(source = "reviewer", target = "reviewer")
	@Mapping(source = "reviewee", target = "reviewee")
	@Mapping(source = "crew", target = "crew")
	@Mapping(source = "crew.promiseTime", target = "promiseTime")
	@Mapping(source = "request.content", target = "content")
	@Mapping(source = "request.mannerScore", target = "mannerScore")
	@Mapping(source = "request", target = "tasteScore", qualifiedByName = "tasteScoreToZero")
	Review toReview(CreateMemberReviewRequest request, User reviewer, User reviewee, Crew crew);

	@Mapping(source = "reviewer", target = "reviewer")
	@Mapping(source = "reviewee", target = "reviewee")
	@Mapping(source = "crew", target = "crew")
	@Mapping(source = "promiseTime", target = "promiseTime")
	@Mapping(source = "content", target = "content")
	@Mapping(source = "mannerScore", target = "mannerScore")
	@Mapping(source = "tasteScore", target = "tasteScore")
	ReviewResponse toReviewResponse(Review review);

	@Mapping(target = "userId", source = "user.id")
	@Mapping(target = "nickname", source = "user.nickname")
	@Mapping(target = "profileImgUrl", source = "user.profileImgUrl")
	@Mapping(target = "crewMemberRole", source = "crewMemberRole")
	@Mapping(target = "isReviewed", source = "isReviewed")
	RevieweeListResponse toRevieweeListResponse(User user, CrewMemberRole crewMemberRole, boolean isReviewed);

	@Named("tasteScoreToZero")
	default Integer tasteScoreNullToZero(CreateMemberReviewRequest request) {
		return 0;
	}
}
