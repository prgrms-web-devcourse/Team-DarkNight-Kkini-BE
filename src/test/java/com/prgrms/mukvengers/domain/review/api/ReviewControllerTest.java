package com.prgrms.mukvengers.domain.review.api;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.*;
import static com.epages.restdocs.apispec.ResourceDocumentation.*;
import static com.epages.restdocs.apispec.ResourceSnippetParameters.*;
import static com.prgrms.mukvengers.utils.CrewObjectProvider.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.epages.restdocs.apispec.Schema;
import com.prgrms.mukvengers.base.ControllerTest;
import com.prgrms.mukvengers.domain.crew.model.Crew;
import com.prgrms.mukvengers.domain.crew.model.vo.CrewStatus;
import com.prgrms.mukvengers.domain.crewmember.model.CrewMember;
import com.prgrms.mukvengers.domain.crewmember.model.vo.CrewMemberRole;
import com.prgrms.mukvengers.domain.review.dto.request.CreateLeaderReviewRequest;
import com.prgrms.mukvengers.domain.review.dto.request.CreateMemberReviewRequest;
import com.prgrms.mukvengers.domain.review.model.Review;
import com.prgrms.mukvengers.domain.user.model.User;
import com.prgrms.mukvengers.utils.CrewMemberObjectProvider;
import com.prgrms.mukvengers.utils.ReviewObjectProvider;
import com.prgrms.mukvengers.utils.UserObjectProvider;

class ReviewControllerTest extends ControllerTest {

	public static final Schema CREATE_LEADER_REVIEW_REQUEST = new Schema("createReviewOfLeaderRequest");
	public static final Schema CREATE_MEMBER_REVIEW_REQUEST = new Schema("createReviewOfMemberRequest");
	public static final Schema SINGLE_REVIEW_DETAIL = new Schema("singleReviewDetail");
	public static final Schema ALL_RECEIVED_REVIEW = new Schema("allReceivedReview");
	public static final Schema ALL_WROTE_REVIEW = new Schema("allWroteReview");

	private User reviewer;
	private User reviewee;
	private Crew crew;

	@BeforeEach
	void setCrew() {
		reviewer = savedUser;
		reviewee = userRepository.save(UserObjectProvider.createUser("kakao_1212"));
		crew = crewRepository.save(createCrew(savedStore, CrewStatus.RECRUITING));
	}

	@Test
	@DisplayName("[성공] 밥모임 방장에 대해 리뷰를 생성할 수 있다")
	void createReviewOfLeader_success() throws Exception {
		// given
		CrewMember crewMemberOfLeader = crewMemberRepository.save(
			CrewMemberObjectProvider.createCrewMember(reviewee.getId(), crew, CrewMemberRole.LEADER));
		crew.addCrewMember(crewMemberOfLeader);

		CrewMember crewMemberOfMember = crewMemberRepository.save(
			CrewMemberObjectProvider.createCrewMember(reviewer.getId(), crew, CrewMemberRole.MEMBER));
		crew.addCrewMember(crewMemberOfMember);

		CreateLeaderReviewRequest leaderReviewRequest = ReviewObjectProvider.createLeaderReviewRequest(
			reviewee.getId());

		String jsonRequest = objectMapper.writeValueAsString(leaderReviewRequest);

		mockMvc.perform(post("/api/v1/crews/{crewId}/reviews/leader", crew.getId())
				.header(HttpHeaders.AUTHORIZATION, BEARER_TYPE + accessToken)
				.content(jsonRequest)
				.contentType(APPLICATION_JSON))
			// when & then
			.andExpect(status().isCreated())
			.andDo(document("review-createReviewOfLeader",
				resource(
					builder()
						.tag(REVIEW)
						.summary("방장에 대한 리뷰 생성 API")
						.requestSchema(CREATE_LEADER_REVIEW_REQUEST)
						.description("방장에 대한 리뷰를 작성합니다.")
						.requestFields(
							fieldWithPath("leaderId").description("방장의 아이디"),
							fieldWithPath("content").description("추가로 작성하고 싶은 내용"),
							fieldWithPath("mannerScore").description("방장에 대한 매너 온도"),
							fieldWithPath("tasteScore").description("방장에 대한 맛잘알 점수"))
						.responseHeaders(
							headerWithName("Location").description("조회해볼 수 있는 요청 주소"))
						.build()
				)
			));
	}

	@Test
	@DisplayName("[성공] 방장이 아닌 밥모임원에 대해 리뷰를 생성할 수 있다")
	void createReviewOfMember_success() throws Exception {
		// given
		CrewMember crewMemberOfMember1 = crewMemberRepository.save(
			CrewMemberObjectProvider.createCrewMember(reviewee.getId(), crew, CrewMemberRole.MEMBER));
		crew.addCrewMember(crewMemberOfMember1);

		CrewMember crewMemberOfMember2 = crewMemberRepository.save(
			CrewMemberObjectProvider.createCrewMember(reviewer.getId(), crew, CrewMemberRole.MEMBER));
		crew.addCrewMember(crewMemberOfMember2);

		CreateMemberReviewRequest memberReviewRequest = ReviewObjectProvider.createMemberReviewRequest(
			reviewee.getId());

		String jsonRequest = objectMapper.writeValueAsString(memberReviewRequest);

		mockMvc.perform(post("/api/v1/crews/{crewId}/reviews/member", crew.getId())
				.header(HttpHeaders.AUTHORIZATION, BEARER_TYPE + accessToken)
				.content(jsonRequest)
				.contentType(APPLICATION_JSON))
			// when & then
			.andExpect(status().isCreated())
			.andDo(document("review-createReviewOfMember",
				resource(
					builder()
						.tag(REVIEW)
						.summary("밥모임원에 대한 리뷰 생성 API")
						.requestSchema(CREATE_MEMBER_REVIEW_REQUEST)
						.description("방장이 아닌 밥모임원에 대한 리뷰를 작성합니다.")
						.requestFields(
							fieldWithPath("revieweeId").description("리뷰이 아이디"),
							fieldWithPath("content").description("추가로 작성하고 싶은 내용"),
							fieldWithPath("mannerScore").description("리뷰이에 대한 매너 온도"))
						.responseHeaders(
							headerWithName("Location").description("조회해볼 수 있는 요청 주소"))
						.build()
				)
			));
	}

	@Test
	@DisplayName("[성공] 리뷰 단건 조회할 수 있다.")
	void getSingleReview() throws Exception {
		// given
		Review createReview = ReviewObjectProvider.createLeaderReview(reviewer, reviewee, crew);
		Review review = reviewRepository.save(createReview);

		mockMvc.perform(get("/api/v1/reviews/{reviewId}", review.getId())
				.header(HttpHeaders.AUTHORIZATION, BEARER_TYPE + accessToken)
				.contentType(APPLICATION_JSON))
			// when & then
			.andExpect(status().isOk())
			.andDo(print())
			.andDo(document("review-getSingleReview",
				resource(
					builder()
						.tag(REVIEW)
						.summary("리뷰 단건 조회 API")
						.responseSchema(SINGLE_REVIEW_DETAIL)
						.description("하나의 리뷰를 상세 조회 합니다.")
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

							fieldWithPath("data.crew.currentMember").type(NULL).description("밥모임의 아이디"),
							fieldWithPath("data.crew.id").type(NUMBER).description("밥모임의 아이디"),
							fieldWithPath("data.crew.name").type(STRING).description("밥모임의 이름"),
							fieldWithPath("data.crew.capacity").type(NUMBER).description("밥모임의 정원"),
							fieldWithPath("data.crew.status").type(STRING).description("밥모임의 모집 상태"),
							fieldWithPath("data.crew.content").type(STRING).description("밥모임에 해당 간략한 설명"),
							fieldWithPath("data.crew.category").type(STRING).description("밥모임의 카테고리"),
							fieldWithPath("data.crew.promiseTime").type(ARRAY).description("밥모임의 약속 시간"),

							fieldWithPath("data.promiseTime").type(ARRAY).description("리뷰하고자 하는 밥모임 약속 시간"),
							fieldWithPath("data.content").type(STRING).description("리뷰하고자 하는 밥모임 간단 한줄 리뷰 "),
							fieldWithPath("data.mannerScore").type(NUMBER).description("리뷰하고자 하는 밥모임 매너 온도"),
							fieldWithPath("data.tasteScore").type(NUMBER).description("리뷰하고자 하는 밥모임 맛잘알 점수")
						)
						.build()
				)
			));
	}

	@Test
	@DisplayName("[성공] 리뷰이 아이디가 사용자 아이디와 같다면 본인에게 작성된 모든 리뷰를 조회할 수 있다.")
	void getAllReceivedReview() throws Exception {
		// given
		List<Review> reviews = ReviewObjectProvider.createReviews(reviewee, reviewer, crew);

		reviewRepository.saveAll(reviews);

		Integer page = 0;

		Integer size = 10;

		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("page", String.valueOf(page));
		params.add("size", String.valueOf(size));

		mockMvc.perform(get("/api/v1/reviews/me")
				.header(HttpHeaders.AUTHORIZATION, BEARER_TYPE + accessToken)
				.params(params)
				.contentType(APPLICATION_JSON))
			// when & then
			.andExpect(status().isOk())
			.andDo(print())
			.andDo(document("review-getAllReceivedReview",
				resource(
					builder()
						.tag(REVIEW)
						.summary("나에 대한 모든 리뷰 조회 API")
						.responseSchema(ALL_RECEIVED_REVIEW)
						.description("나에게 작성된 모든 리뷰를 조회합니다.")
						.responseFields(
							fieldWithPath("data.content.[].reviewer.id").type(NUMBER)
								.description("리뷰를 작성하고자 하는 사용자의 아이디"),
							fieldWithPath("data.content.[].reviewer.nickname").type(STRING)
								.description("리뷰를 작성하고자 하는 사용자의 닉네임"),
							fieldWithPath("data.content.[].reviewer.profileImgUrl").type(STRING)
								.description("리뷰를 작성하고자 하는 사용자의 프로필 URL"),
							fieldWithPath("data.content.[].reviewer.introduction").type(STRING)
								.description("리뷰를 작성하고자 하는 사용자의 자기 소개"),
							fieldWithPath("data.content.[].reviewer.leaderCount").type(NUMBER)
								.description("리뷰를 작성하고자 하는 사용자의 리더 횟수"),
							fieldWithPath("data.content.[].reviewer.crewCount").type(NUMBER)
								.description("리뷰를 작성하고자 하는 사용자의 밥모임 참여 횟수"),
							fieldWithPath("data.content.[].reviewer.tasteScore").type(NUMBER)
								.description("리뷰를 작성하고자 하는 사용자의 맛잘알 점수"),
							fieldWithPath("data.content.[].reviewer.mannerScore").type(NUMBER)
								.description("리뷰를 작성하고자 하는 사용자의 매너 온도 점수"),

							fieldWithPath("data.content.[].reviewee.id").type(NUMBER).description("리뷰 남기고자하는 사용자의 아이디"),
							fieldWithPath("data.content.[].reviewee.nickname").type(STRING)
								.description("리뷰 남기고자하는 사용자의 닉네임"),
							fieldWithPath("data.content.[].reviewee.profileImgUrl").type(STRING)
								.description("리뷰 남기고자하는 사용자의 프로필 URL"),
							fieldWithPath("data.content.[].reviewee.introduction").type(STRING)
								.description("리뷰 남기고자하는 사용자의 자기 소개"),
							fieldWithPath("data.content.[].reviewee.leaderCount").type(NUMBER)
								.description("리뷰 남기고자하는 사용자의 리더 횟수"),
							fieldWithPath("data.content.[].reviewee.crewCount").type(NUMBER)
								.description("리뷰 남기고자하는 사용자의 밥모임 참여 횟수"),
							fieldWithPath("data.content.[].reviewee.tasteScore").type(NUMBER)
								.description("리뷰 남기고자하는 사용자의 맛잘알 점수"),
							fieldWithPath("data.content.[].reviewee.mannerScore").type(NUMBER)
								.description("리뷰 남기고자하는 사용자의 매너 온도 점수"),

							fieldWithPath("data.content.[].crew.id").type(NUMBER).description("리뷰하고자하는 밥 모임 아이디"),
							fieldWithPath("data.content.[].crew.name").type(STRING).description("리뷰하고자하는 밥 모임 이름"),
							fieldWithPath("data.content.[].crew.capacity").type(NUMBER).description("리뷰하고자하는 밥 모임 정원"),
							fieldWithPath("data.content.[].crew.promiseTime").type(ARRAY).description("리뷰하고자하는 약속 시간"),
							fieldWithPath("data.content.[].crew.status").type(STRING).description("리뷰하고자하는 밥 모임 상태"),
							fieldWithPath("data.content.[].crew.currentMember").description("리뷰하고자하는 밥 모임 현재 인원 수"),
							fieldWithPath("data.content.[].crew.content").type(STRING).description("리뷰하고자하는 밥 모임 내용"),
							fieldWithPath("data.content.[].crew.category").type(STRING)
								.description("리뷰하고자하는 밥 모임 카테고리"),

							fieldWithPath("data.content.[].promiseTime").type(ARRAY).description("리뷰하고자 하는 밥 모임 약속 시간"),
							fieldWithPath("data.content.[].content").type(STRING).description("리뷰하고자 하는 리뷰이에 대한 설명"),
							fieldWithPath("data.content.[].mannerScore").type(NUMBER)
								.description("리뷰하고자 하는 리뷰이에 대한 매너 점수"),
							fieldWithPath("data.content.[].tasteScore").type(NUMBER)
								.description("리뷰하고자 하는 리뷰이에 대한 맛잘알 점수"),

							fieldWithPath("data.pageable.sort.empty").type(BOOLEAN).description("빈 페이지 여부"),
							fieldWithPath("data.pageable.sort.sorted").type(BOOLEAN).description("페이지 정렬 여부"),
							fieldWithPath("data.pageable.sort.unsorted").type(BOOLEAN)
								.description("페이지 비정렬 여부"),
							fieldWithPath("data.pageable.offset").type(NUMBER).description("페이지 오프셋"),
							fieldWithPath("data.pageable.pageNumber").type(NUMBER).description("페이지 번호"),
							fieldWithPath("data.pageable.pageSize").type(NUMBER)
								.description("한 원소 수"),
							fieldWithPath("data.pageable.paged").type(BOOLEAN).description("페이지 정보 포함 여부"),
							fieldWithPath("data.pageable.unpaged").type(BOOLEAN).description("페이지 정보 비포함 여부"),
							fieldWithPath("data.last").type(BOOLEAN).description("마지막 페이지 여부"),
							fieldWithPath("data.size").type(NUMBER).description("페이지 사이즈"),
							fieldWithPath("data.number").type(NUMBER).description("페이지 번호"),
							fieldWithPath("data.sort.empty").type(BOOLEAN).description("빈 페이지 여부"),
							fieldWithPath("data.sort.sorted").type(BOOLEAN).description("페이지 정렬 여부"),
							fieldWithPath("data.sort.unsorted").type(BOOLEAN).description("페이지 비정렬 여부"),
							fieldWithPath("data.first").type(BOOLEAN).description("첫 번째 페이지 여부"),
							fieldWithPath("data.numberOfElements").type(NUMBER).description("페이지 원소 개수"),
							fieldWithPath("data.empty").type(BOOLEAN).description("빈 페이지 여부"),
							fieldWithPath("data.totalPages").type(NUMBER).description("전체 페이지 개수"),
							fieldWithPath("data.totalElements").type(NUMBER).description("전체 데이터 개수"))
						.build()
				)
			));
	}

	@Test
	@DisplayName("[성공] 리뷰어 아이디가 사용자 아이디와 같다면 사용자가 다른 사람에게 작성한 모든 리뷰를 조회할 수 있다.")
	void getAllWroteReview() throws Exception {
		// given
		List<Review> reviews = ReviewObjectProvider.createReviews(reviewer, reviewee, crew);

		reviewRepository.saveAll(reviews);

		Integer page = 0;

		Integer size = 10;

		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("page", String.valueOf(page));
		params.add("size", String.valueOf(size));

		mockMvc.perform(get("/api/v1/reviews")
				.header(HttpHeaders.AUTHORIZATION, BEARER_TYPE + accessToken)
				.params(params)
				.contentType(APPLICATION_JSON))
			// when & then
			.andExpect(status().isOk())
			.andDo(print())
			.andDo(document("review-getAllWroteReview",
				resource(
					builder()
						.tag(REVIEW)
						.summary("내가 작성한 모든 리뷰 조회 API")
						.responseSchema(ALL_WROTE_REVIEW)
						.description("다른 밥모임원에게 작성한 모든 리뷰를 조회합니다.")
						.responseFields(
							fieldWithPath("data.content.[].reviewer.id").type(NUMBER)
								.description("리뷰를 작성하고자 하는 사용자의 아이디"),
							fieldWithPath("data.content.[].reviewer.nickname").type(STRING)
								.description("리뷰를 작성하고자 하는 사용자의 닉네임"),
							fieldWithPath("data.content.[].reviewer.profileImgUrl").type(STRING)
								.description("리뷰를 작성하고자 하는 사용자의 프로필 URL"),
							fieldWithPath("data.content.[].reviewer.introduction").type(STRING)
								.description("리뷰를 작성하고자 하는 사용자의 자기 소개"),
							fieldWithPath("data.content.[].reviewer.leaderCount").type(NUMBER)
								.description("리뷰를 작성하고자 하는 사용자의 리더 횟수"),
							fieldWithPath("data.content.[].reviewer.crewCount").type(NUMBER)
								.description("리뷰를 작성하고자 하는 사용자의 밥모임 참여 횟수"),
							fieldWithPath("data.content.[].reviewer.tasteScore").type(NUMBER)
								.description("리뷰를 작성하고자 하는 사용자의 맛잘알 점수"),
							fieldWithPath("data.content.[].reviewer.mannerScore").type(NUMBER)
								.description("리뷰를 작성하고자 하는 사용자의 매너 온도 점수"),

							fieldWithPath("data.content.[].reviewee.id").type(NUMBER).description("리뷰 남기고자하는 사용자의 아이디"),
							fieldWithPath("data.content.[].reviewee.nickname").type(STRING)
								.description("리뷰 남기고자하는 사용자의 닉네임"),
							fieldWithPath("data.content.[].reviewee.profileImgUrl").type(STRING)
								.description("리뷰 남기고자하는 사용자의 프로필 URL"),
							fieldWithPath("data.content.[].reviewee.introduction").type(STRING)
								.description("리뷰 남기고자하는 사용자의 자기 소개"),
							fieldWithPath("data.content.[].reviewee.leaderCount").type(NUMBER)
								.description("리뷰 남기고자하는 사용자의 리더 횟수"),
							fieldWithPath("data.content.[].reviewee.crewCount").type(NUMBER)
								.description("리뷰 남기고자하는 사용자의 밥모임 참여 횟수"),
							fieldWithPath("data.content.[].reviewee.tasteScore").type(NUMBER)
								.description("리뷰 남기고자하는 사용자의 맛잘알 점수"),
							fieldWithPath("data.content.[].reviewee.mannerScore").type(NUMBER)
								.description("리뷰 남기고자하는 사용자의 매너 온도 점수"),

							fieldWithPath("data.content.[].crew.id").type(NUMBER).description("리뷰하고자하는 밥 모임 아이디"),
							fieldWithPath("data.content.[].crew.name").type(STRING).description("리뷰하고자하는 밥 모임 이름"),
							fieldWithPath("data.content.[].crew.capacity").type(NUMBER).description("리뷰하고자하는 밥 모임 정원"),
							fieldWithPath("data.content.[].crew.promiseTime").type(ARRAY).description("리뷰하고자하는 약속 시간"),
							fieldWithPath("data.content.[].crew.status").type(STRING).description("리뷰하고자하는 밥 모임 상태"),
							fieldWithPath("data.content.[].crew.currentMember").description("리뷰하고자하는 밥 모임 현재 인원 수"),
							fieldWithPath("data.content.[].crew.content").type(STRING).description("리뷰하고자하는 밥 모임 내용"),
							fieldWithPath("data.content.[].crew.category").type(STRING)
								.description("리뷰하고자하는 밥 모임 카테고리"),

							fieldWithPath("data.content.[].promiseTime").type(ARRAY).description("리뷰하고자 하는 밥 모임 약속 시간"),
							fieldWithPath("data.content.[].content").type(STRING).description("리뷰하고자 하는 리뷰이에 대한 설명"),
							fieldWithPath("data.content.[].mannerScore").type(NUMBER)
								.description("리뷰하고자 하는 리뷰이에 대한 매너 점수"),
							fieldWithPath("data.content.[].tasteScore").type(NUMBER)
								.description("리뷰하고자 하는 리뷰이에 대한 맛잘알 점수"),

							fieldWithPath("data.pageable.sort.empty").type(BOOLEAN).description("빈 페이지 여부"),
							fieldWithPath("data.pageable.sort.sorted").type(BOOLEAN).description("페이지 정렬 여부"),
							fieldWithPath("data.pageable.sort.unsorted").type(BOOLEAN)
								.description("페이지 비정렬 여부"),
							fieldWithPath("data.pageable.offset").type(NUMBER).description("페이지 오프셋"),
							fieldWithPath("data.pageable.pageNumber").type(NUMBER).description("페이지 번호"),
							fieldWithPath("data.pageable.pageSize").type(NUMBER)
								.description("한 원소 수"),
							fieldWithPath("data.pageable.paged").type(BOOLEAN).description("페이지 정보 포함 여부"),
							fieldWithPath("data.pageable.unpaged").type(BOOLEAN).description("페이지 정보 비포함 여부"),
							fieldWithPath("data.last").type(BOOLEAN).description("마지막 페이지 여부"),
							fieldWithPath("data.size").type(NUMBER).description("페이지 사이즈"),
							fieldWithPath("data.number").type(NUMBER).description("페이지 번호"),
							fieldWithPath("data.sort.empty").type(BOOLEAN).description("빈 페이지 여부"),
							fieldWithPath("data.sort.sorted").type(BOOLEAN).description("페이지 정렬 여부"),
							fieldWithPath("data.sort.unsorted").type(BOOLEAN).description("페이지 비정렬 여부"),
							fieldWithPath("data.first").type(BOOLEAN).description("첫 번째 페이지 여부"),
							fieldWithPath("data.numberOfElements").type(NUMBER).description("페이지 원소 개수"),
							fieldWithPath("data.empty").type(BOOLEAN).description("빈 페이지 여부"),
							fieldWithPath("data.totalPages").type(NUMBER).description("전체 페이지 개수"),
							fieldWithPath("data.totalElements").type(NUMBER).description("전체 데이터 개수"))
						.build()
				)
			));
	}
}