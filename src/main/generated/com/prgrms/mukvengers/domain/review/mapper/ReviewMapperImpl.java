package com.prgrms.mukvengers.domain.review.mapper;

import com.prgrms.mukvengers.domain.crew.dto.response.CrewResponse;
import com.prgrms.mukvengers.domain.crew.model.Crew;
import com.prgrms.mukvengers.domain.crew.model.vo.CrewStatus;
import com.prgrms.mukvengers.domain.crewmember.model.vo.CrewMemberRole;
import com.prgrms.mukvengers.domain.review.dto.request.CreateLeaderReviewRequest;
import com.prgrms.mukvengers.domain.review.dto.request.CreateMemberReviewRequest;
import com.prgrms.mukvengers.domain.review.dto.response.ReviewResponse;
import com.prgrms.mukvengers.domain.review.dto.response.RevieweeListResponse;
import com.prgrms.mukvengers.domain.review.model.Review;
import com.prgrms.mukvengers.domain.user.dto.response.UserProfileResponse;
import com.prgrms.mukvengers.domain.user.model.User;
import java.time.LocalDateTime;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-08-10T11:38:02+0900",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.8 (Amazon.com Inc.)"
)
@Component
public class ReviewMapperImpl implements ReviewMapper {

    @Override
    public Review toReview(CreateLeaderReviewRequest request, User reviewer, User reviewee, Crew crew) {
        if ( request == null && reviewer == null && reviewee == null && crew == null ) {
            return null;
        }

        Review.ReviewBuilder review = Review.builder();

        if ( request != null ) {
            review.content( request.content() );
            review.mannerScore( request.mannerScore() );
            review.tasteScore( request.tasteScore() );
        }
        if ( crew != null ) {
            review.crew( crew );
            review.promiseTime( crew.getPromiseTime() );
        }
        review.reviewer( reviewer );
        review.reviewee( reviewee );

        return review.build();
    }

    @Override
    public Review toReview(CreateMemberReviewRequest request, User reviewer, User reviewee, Crew crew) {
        if ( request == null && reviewer == null && reviewee == null && crew == null ) {
            return null;
        }

        Review.ReviewBuilder review = Review.builder();

        if ( request != null ) {
            review.content( request.content() );
            review.mannerScore( request.mannerScore() );
            review.tasteScore( tasteScoreNullToZero( request ) );
        }
        if ( crew != null ) {
            review.crew( crew );
            review.promiseTime( crew.getPromiseTime() );
        }
        review.reviewer( reviewer );
        review.reviewee( reviewee );

        return review.build();
    }

    @Override
    public ReviewResponse toReviewResponse(Review review) {
        if ( review == null ) {
            return null;
        }

        UserProfileResponse reviewer = null;
        UserProfileResponse reviewee = null;
        CrewResponse crew = null;
        LocalDateTime promiseTime = null;
        String content = null;
        Integer mannerScore = null;
        Integer tasteScore = null;

        reviewer = userToUserProfileResponse( review.getReviewer() );
        reviewee = userToUserProfileResponse( review.getReviewee() );
        crew = crewToCrewResponse( review.getCrew() );
        promiseTime = review.getPromiseTime();
        content = review.getContent();
        mannerScore = review.getMannerScore();
        tasteScore = review.getTasteScore();

        ReviewResponse reviewResponse = new ReviewResponse( reviewer, reviewee, crew, promiseTime, content, mannerScore, tasteScore );

        return reviewResponse;
    }

    @Override
    public RevieweeListResponse toRevieweeListResponse(User user, CrewMemberRole crewMemberRole, boolean isReviewed) {
        if ( user == null && crewMemberRole == null ) {
            return null;
        }

        Long userId = null;
        String nickname = null;
        String profileImgUrl = null;
        if ( user != null ) {
            userId = user.getId();
            nickname = user.getNickname();
            profileImgUrl = user.getProfileImgUrl();
        }
        CrewMemberRole crewMemberRole1 = null;
        crewMemberRole1 = crewMemberRole;
        boolean isReviewed1 = false;
        isReviewed1 = isReviewed;

        RevieweeListResponse revieweeListResponse = new RevieweeListResponse( userId, nickname, profileImgUrl, crewMemberRole1, isReviewed1 );

        return revieweeListResponse;
    }

    protected UserProfileResponse userToUserProfileResponse(User user) {
        if ( user == null ) {
            return null;
        }

        Long id = null;
        String nickname = null;
        String profileImgUrl = null;
        String introduction = null;
        Integer leaderCount = null;
        Integer crewCount = null;
        Integer tasteScore = null;
        String mannerScore = null;

        id = user.getId();
        nickname = user.getNickname();
        profileImgUrl = user.getProfileImgUrl();
        introduction = user.getIntroduction();
        leaderCount = user.getLeaderCount();
        crewCount = user.getCrewCount();
        tasteScore = user.getTasteScore();
        if ( user.getMannerScore() != null ) {
            mannerScore = user.getMannerScore().toString();
        }

        UserProfileResponse userProfileResponse = new UserProfileResponse( id, nickname, profileImgUrl, introduction, leaderCount, crewCount, tasteScore, mannerScore );

        return userProfileResponse;
    }

    protected CrewResponse crewToCrewResponse(Crew crew) {
        if ( crew == null ) {
            return null;
        }

        Long id = null;
        String name = null;
        Integer capacity = null;
        CrewStatus status = null;
        String content = null;
        String category = null;
        LocalDateTime promiseTime = null;

        id = crew.getId();
        name = crew.getName();
        capacity = crew.getCapacity();
        status = crew.getStatus();
        content = crew.getContent();
        category = crew.getCategory();
        promiseTime = crew.getPromiseTime();

        Integer currentMember = null;

        CrewResponse crewResponse = new CrewResponse( id, name, currentMember, capacity, status, content, category, promiseTime );

        return crewResponse;
    }
}
