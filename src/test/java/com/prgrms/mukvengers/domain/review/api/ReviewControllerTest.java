package com.prgrms.mukvengers.domain.review.api;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.*;
import static com.epages.restdocs.apispec.ResourceDocumentation.*;
import static com.epages.restdocs.apispec.ResourceSnippetParameters.*;
import static com.prgrms.mukvengers.utils.CrewObjectProvider.*;
import static com.prgrms.mukvengers.utils.ReviewObjectProvider.*;
import static com.prgrms.mukvengers.utils.UserObjectProvider.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;

import com.prgrms.mukvengers.base.ControllerTest;
import com.prgrms.mukvengers.domain.crew.model.Crew;
import com.prgrms.mukvengers.domain.crewmember.model.CrewMember;
import com.prgrms.mukvengers.domain.review.dto.request.CreateLeaderReviewRequest;
import com.prgrms.mukvengers.domain.review.dto.request.CreateMemberReviewRequest;
import com.prgrms.mukvengers.domain.review.model.Review;
import com.prgrms.mukvengers.domain.review.repository.ReviewRepository;
import com.prgrms.mukvengers.domain.user.model.User;
import com.prgrms.mukvengers.utils.ReviewObjectProvider;

class ReviewControllerTest extends ControllerTest {

	private static final String REVIEW = "리뷰 API";
	@Autowired
	ReviewController reviewController;

	@Autowired
	ReviewRepository reviewRepository;

	User reviewer = savedUser;
	User reviewee;
	Crew crew;
	CrewMember crewMember;

	@BeforeEach
	void setCrew() {
		reviewer = userRepository.save(savedUser);
		reviewee = userRepository.save(createUser());
		crew = crewRepository.save(createCrew(reviewee, savedStore));

		//crewMemberObjectProvider 변경 예정
		CrewMember createCrewMember = CrewMember.builder()
			.crew(crew)
			.user(reviewer)
			.ready(false)
			.blocked(false)
			.build();

		crewMember = crewMemberRepository.save(createCrewMember);
		crew.addCrewMember(crewMember);
	}

	@Test
	@DisplayName("[성공] 밥모임 방장에 대해 리뷰를 생성할 수 있다")
	void createReviewOfLeader_success() throws Exception {

		CreateLeaderReviewRequest leaderReviewRequest = ReviewObjectProvider.createLeaderReviewRequest(
			reviewee.getId());
		String jsonRequest = objectMapper.writeValueAsString(leaderReviewRequest);

		mockMvc.perform(post("/api/v1/crews/{crewId}/reviews/leader", crew.getId())
				.header(HttpHeaders.AUTHORIZATION, BEARER_TYPE + accessToken)
				.contentType(APPLICATION_JSON)
				.content(jsonRequest))
			.andExpect(status().isCreated())
			.andDo(document("reviewOfLeader-create",
				resource(
					builder()
						.tag(REVIEW)
						.summary("방장에 대한 리뷰 생성")
						.requestFields(
							fieldWithPath("leaderId").description("방장의 아이디"),
							fieldWithPath("content").description("추가로 작성하고 싶은 내용"),
							fieldWithPath("mannerPoint").description("방장에 대한 매너 온도"),
							fieldWithPath("tastePoint").description("방장에 대한 맛잘알 점수"))
						.responseFields()
						.build()
				)
			));
	}

	@Test
	@DisplayName("[성공] 방장이 아닌 밥모임원에 대해 리뷰를 생성할 수 있다")
	void createReviewOfMember_success() throws Exception {

		CreateMemberReviewRequest memberReviewRequest = createReviewOfMemberRequest();

		String jsonRequest = objectMapper.writeValueAsString(memberReviewRequest);

		mockMvc.perform(post("/api/v1/crews/{crewId}/reviews/member", crew.getId())
				.header(HttpHeaders.AUTHORIZATION, BEARER_TYPE + accessToken)
				.contentType(APPLICATION_JSON)
				.content(jsonRequest))
			.andExpect(status().isCreated())
			.andDo(document("reviewOfMember-create",
				resource(
					builder()
						.tag(REVIEW)
						.summary("방장이 아닌 밥모임원에 대한 리뷰 생성")
						.requestFields(
							fieldWithPath("revieweeId").description("리뷰이 아이디"),
							fieldWithPath("content").description("추가로 작성하고 싶은 내용"),
							fieldWithPath("mannerPoint").description("리뷰이에 대한 매너 온도"))
						.responseFields()
						.build()
				)
			));
	}

	@Test
	@DisplayName("[성공] 리뷰 단건 조회할 수 있다.")
	void getSingleReview() throws Exception {

		Review createReview = createLeaderReview(reviewer, reviewee, savedStore);
		Review review = reviewRepository.save(createReview);

		mockMvc.perform(get("/api/v1/reviews/{reviewId}", review.getId())
				.header(HttpHeaders.AUTHORIZATION, BEARER_TYPE + accessToken)
				.contentType(APPLICATION_JSON))
			.andExpect(status().isOk())
			.andDo(print())
			.andDo(document("singleReview",
				resource(
					builder()
						.tag(REVIEW)
						.summary("리뷰 단건 조회")
						.responseFields(
							fieldWithPath("data.reviewer.id").description("리뷰어의 아이디"),
							fieldWithPath("data.reviewer.nickname").description("리뷰어의 닉네임"),
							fieldWithPath("data.reviewer.profileImgUrl").description("리뷰어의 프로필 이미지 주소"),
							fieldWithPath("data.reviewer.introduction").description("리뷰어의 한줄 소개"),
							fieldWithPath("data.reviewer.leaderCount").description("리뷰어의 리더 횟수"),
							fieldWithPath("data.reviewer.crewCount").description("리뷰어의 밥모임 참여한 카운트"),
							fieldWithPath("data.reviewer.tasteScore").description("리뷰어의 맛잘알 점수"),
							fieldWithPath("data.reviewer.mannerScore").description("리뷰어의 매너 점수"),

							fieldWithPath("data.reviewee.id").type(NUMBER).description("리뷰이의 아이디"),
							fieldWithPath("data.reviewee.nickname").type(STRING).description("리뷰이의 닉네임"),
							fieldWithPath("data.reviewee.profileImgUrl").type(STRING).description("리뷰이의 프로필 이미지 주소"),
							fieldWithPath("data.reviewee.introduction").type(STRING).description("리뷰이의 한줄 소개"),
							fieldWithPath("data.reviewee.leaderCount").type(NUMBER).description("리뷰이의 리더 횟수"),
							fieldWithPath("data.reviewee.crewCount").type(NUMBER).description("리뷰이의 밥모임 참여한 횟수"),
							fieldWithPath("data.reviewee.tasteScore").type(NUMBER).description("리뷰이의 맛잘알 점수"),
							fieldWithPath("data.reviewee.mannerScore").type(NUMBER).description("리뷰이의 매너 점수"),

							fieldWithPath("data.store.id").type(NUMBER).description("가게의 아이디"),
							fieldWithPath("data.store.latitude").type(STRING).description("가게의 위도"),
							fieldWithPath("data.store.longitude").type(STRING).description("가게의 경도"),
							fieldWithPath("data.store.mapStoreId").type(STRING).description("지도 api 제공 id"),

							fieldWithPath("data.crewName").type(STRING).description("리뷰하고자 하는 밥모임 이름"),
							fieldWithPath("data.promiseTime").type(ARRAY).description("리뷰하고자 하는 밥모임 약속 시간"),
							fieldWithPath("data.content").type(STRING).description("리뷰하고자 하는 밥모임 간단 한줄 리뷰 "),
							fieldWithPath("data.mannerPoint").type(NUMBER).description("리뷰하고자 하는 밥모임 매너 온도"),
							fieldWithPath("data.tastePoint").type(NUMBER).description("리뷰하고자 하는 밥모임 맛잘알 점수")
						)
						.build()
				)
			));
	}

	@NotNull
	private CreateMemberReviewRequest createReviewOfMemberRequest() {
		User otherCrewOne = userRepository.save(createUser());
		CrewMember createCrewMember = CrewMember.builder()
			.user(otherCrewOne)
			.crew(crew)
			.ready(false)
			.blocked(false)
			.build();

		CrewMember otherCrewMember = crewMemberRepository.save(createCrewMember);
		crew.addCrewMember(otherCrewMember);

		return createMemberReviewRequest(
			otherCrewOne.getId());
	}
}