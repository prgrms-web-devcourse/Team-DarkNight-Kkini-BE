package com.prgrms.mukvengers.domain.review.mapper;

import static org.mapstruct.ReportingPolicy.*;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.prgrms.mukvengers.domain.crew.model.Crew;
import com.prgrms.mukvengers.domain.review.dto.request.CreateLeaderReviewRequest;
import com.prgrms.mukvengers.domain.review.dto.request.CreateMemberReviewRequest;
import com.prgrms.mukvengers.domain.review.dto.response.ReviewResponse;
import com.prgrms.mukvengers.domain.review.model.Review;
import com.prgrms.mukvengers.domain.user.model.User;

@Mapper(componentModel = "spring", unmappedTargetPolicy = IGNORE)
public interface ReviewMapper {

	@Mapping(source = "reviewer", target = "reviewer")
	@Mapping(source = "reviewee", target = "reviewee")
	@Mapping(source = "crew.store", target = "store")
	@Mapping(source = "crew.name", target = "crewName")
	@Mapping(source = "crew.promiseTime", target = "promiseTime")
	@Mapping(source = "request.content", target = "content")
	@Mapping(source = "request.mannerPoint", target = "mannerPoint")
	@Mapping(source = "request.tastePoint", target = "tastePoint")
	Review toReview(CreateLeaderReviewRequest request, User reviewer, User reviewee, Crew crew);

	@Mapping(source = "reviewer", target = "reviewer")
	@Mapping(source = "reviewee", target = "reviewee")
	@Mapping(source = "crew.store", target = "store")
	@Mapping(source = "crew.name", target = "crewName")
	@Mapping(source = "crew.promiseTime", target = "promiseTime")
	@Mapping(source = "request.content", target = "content")
	@Mapping(source = "request.mannerPoint", target = "mannerPoint")
	Review toReview(CreateMemberReviewRequest request, User reviewer, User reviewee, Crew crew);

	@Mapping(source = "reviewer", target = "reviewer")
	@Mapping(source = "reviewee", target = "reviewee")
	@Mapping(source = "review", target = "store.id", qualifiedByName = "storeIdMethod")
	@Mapping(source = "review", target = "store.latitude", qualifiedByName = "latitudeMethod")
	@Mapping(source = "review", target = "store.longitude", qualifiedByName = "longitudeMethod")
	@Mapping(source = "review", target = "store.mapStoreId", qualifiedByName = "storeMapStoreId")
	@Mapping(source = "crewName", target = "crewName")
	@Mapping(source = "promiseTime", target = "promiseTime")
	@Mapping(source = "content", target = "content")
	@Mapping(source = "mannerPoint", target = "mannerPoint")
	@Mapping(source = "tastePoint", target = "tastePoint")
	ReviewResponse toReviewResponse(Review review);

	@Named("latitudeMethod")
	default String mapLatitude(Review review) {
		return String.valueOf(review.getStore().getLocation().getX());
	}

	@Named("longitudeMethod")
	default String mapLongitude(Review review) {
		return String.valueOf(review.getStore().getLocation().getY());
	}

	@Named("storeIdMethod")
	default String mapStoreId(Review review) {
		return String.valueOf(review.getStore().getId());
	}

	@Named("storeMapStoreId")
	default String mapStoreMapStoreId(Review review) {
		return String.valueOf(review.getStore().getId());
	}
}
