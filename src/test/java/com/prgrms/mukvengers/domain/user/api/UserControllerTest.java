package com.prgrms.mukvengers.domain.user.api;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.*;
import static com.epages.restdocs.apispec.ResourceDocumentation.*;
import static com.epages.restdocs.apispec.ResourceSnippetParameters.*;
import static com.prgrms.mukvengers.utils.UserObjectProvider.*;
import static org.springframework.http.HttpHeaders.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.epages.restdocs.apispec.Schema;
import com.prgrms.mukvengers.base.ControllerTest;
import com.prgrms.mukvengers.domain.user.dto.request.UpdateUserRequest;
import com.prgrms.mukvengers.domain.user.model.User;

class UserControllerTest extends ControllerTest {

	private static final Schema userResponseSchema = new Schema("myProfileResponse");

	@Test
	@DisplayName("[성공] 사용자는 자신의 프로필 정보를 확인할 수 있다")
	void getMyProfile_success() throws Exception {
		// when
		mockMvc.perform(get("/api/v1/user/me")
				.header(AUTHORIZATION, BEARER_TYPE + accessToken))
			// then
			.andExpectAll(
				handler().methodName("getMyProfile"),
				status().isOk())
			.andExpect(jsonPath("$.data").exists())
			.andDo(print())
			// docs
			.andDo(document("getMyProfile",
				resource(
					builder()
						.tags(USER)
						.summary("내 정보 조회 API")
						.responseSchema(userResponseSchema)
						.description("내 정보를 조회합니다.")
						.requestHeaders(
							headerWithName(AUTHORIZATION).description("accessToken"))
						.responseFields(
							fieldWithPath("data.id").type(NUMBER).description("유저 ID"),
							fieldWithPath("data.nickname").type(STRING).description("닉네임"),
							fieldWithPath("data.profileImgUrl").type(STRING).description("프로필 이미지"),
							fieldWithPath("data.introduction").type(STRING).description("한줄 소개"),
							fieldWithPath("data.leaderCount").type(NUMBER).description("방장 횟수"),
							fieldWithPath("data.leaderCount").type(NUMBER).description("방장 횟수"),
							fieldWithPath("data.crewCount").type(NUMBER).description("모임 참여 횟수"),
							fieldWithPath("data.tasteScore").type(NUMBER).description("맛잘알 점수"),
							fieldWithPath("data.mannerScore").type(NUMBER).description("매너 온도"))
						.build()
				))
			);
	}

	@Test
	@DisplayName("[성공] 유저 아이디로 유저 정보를 조회할 수 있다.")
	void getUserProfile_success() throws Exception {
		// when
		mockMvc.perform(get("/api/v1/user/{userId}", savedUserId)
				.header(AUTHORIZATION, BEARER_TYPE + accessToken)
			)
			// then
			.andExpectAll(
				handler().methodName("getUserProfile"),
				status().isOk())
			.andExpect(jsonPath("$.data").exists())
			.andDo(print())
			// docs
			.andDo(document("getUserProfile",
				resource(
					builder()
						.tags(USER)
						.summary("내 정보 조회 API")
						.responseSchema(userResponseSchema)
						.description("유저 아이디로 공개된 유저 정보를 조회합니다.")
						.pathParameters(
							parameterWithName("userId").description("조회하고자 하는 유저의 아이디"))
						.responseFields(
							fieldWithPath("data.id").type(NUMBER).description("유저 ID"),
							fieldWithPath("data.nickname").type(STRING).description("닉네임"),
							fieldWithPath("data.profileImgUrl").type(STRING).description("프로필 이미지"),
							fieldWithPath("data.introduction").type(STRING).description("한줄 소개"),
							fieldWithPath("data.leaderCount").type(NUMBER).description("방장 횟수"),
							fieldWithPath("data.leaderCount").type(NUMBER).description("방장 횟수"),
							fieldWithPath("data.crewCount").type(NUMBER).description("모임 참여 횟수"),
							fieldWithPath("data.tasteScore").type(NUMBER).description("맛잘알 점수"),
							fieldWithPath("data.mannerScore").type(NUMBER).description("매너 온도"))
						.build()
				))
			);
	}

	@Test
	@DisplayName("[성공] 자신의 프로필을 수정할 수 있다.")
	void updateMyProfile_success() throws Exception {
		// given
		UpdateUserRequest request = getUpdateUserRequest();

		// when
		mockMvc.perform(put("/api/v1/user/me")
				.header(AUTHORIZATION, BEARER_TYPE + accessToken)
				.contentType(APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request))
			)
			// then
			.andExpectAll(
				handler().methodName("updateMyProfile"),
				status().isOk())
			.andExpect(jsonPath("$.data").exists())
			.andDo(print())
			// docs
			.andDo(document("updateMyProfile",
				resource(
					builder()
						.tags(USER)
						.summary("내 정보 수정 API")
						.responseSchema(userResponseSchema)
						.description("내 정보를 수정합니다.")
						.responseFields(
							fieldWithPath("data.id").type(NUMBER).description("유저 ID"),
							fieldWithPath("data.nickname").type(STRING).description("닉네임"),
							fieldWithPath("data.profileImgUrl").type(STRING).description("프로필 이미지"),
							fieldWithPath("data.introduction").type(STRING).description("한줄 소개"),
							fieldWithPath("data.leaderCount").type(NUMBER).description("방장 횟수"),
							fieldWithPath("data.leaderCount").type(NUMBER).description("방장 횟수"),
							fieldWithPath("data.crewCount").type(NUMBER).description("모임 참여 횟수"),
							fieldWithPath("data.tasteScore").type(NUMBER).description("맛잘알 점수"),
							fieldWithPath("data.mannerScore").type(NUMBER).description("매너 온도"))
						.build()
				))
			);
	}

	@Test
	@DisplayName("자신의 계정을 삭제할 수 있다")
	void deleteMyProfile_success() throws Exception {

		// given
		User createdUser = User.builder()
			.nickname(DEFAULT_NICKNAME)
			.profileImgUrl(DEFAULT_PROFILE_IMG_URL)
			.provider(PROVIDER_KAKAO)
			.oauthId(OAUTH_ID)
			.build();
		User savedUser = userRepository.save(createdUser);

		Long userId = savedUser.getId();

		String accessToken = jwtTokenProvider.createAccessToken(userId, "USER");

		// when
		mockMvc.perform(delete("/api/v1/user/me")
				.header(AUTHORIZATION, BEARER_TYPE + accessToken))
			// then
			.andExpectAll(
				handler().methodName("deleteMyProfile"),
				status().isNoContent())
			.andDo(print())
			.andDo(document("deleteMyProfile",
				resource(
					builder()
						.tags(USER)
						.summary("내 정보 삭제 API")
						.description("내 정보를 삭제합니다.")
						.responseSchema(new Schema("NONE"))
						.build()
				))
			);

	}
}
