package com.prgrms.mukvengers.domain.user.api;

import static com.prgrms.mukvengers.utils.UserObjectProvider.*;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;

import com.prgrms.mukvengers.base.ControllerTest;
import com.prgrms.mukvengers.domain.user.dto.request.UpdateUserRequest;
import com.prgrms.mukvengers.domain.user.model.User;

class UserControllerTest extends ControllerTest {

	@Test
	@DisplayName("[성공] 사용자는 자신의 프로필 정보를 확인할 수 있다")
	void getMyProfileSuccessTest() throws Exception {
		// when
		mockMvc.perform(RestDocumentationRequestBuilders.get("/api/v1/user/me")
				.header(HttpHeaders.AUTHORIZATION, BEARER_TYPE + ACCESS_TOKEN)
			)
			// then
			.andExpectAll(
				handler().methodName("getMyProfile"),
				status().isOk())
			// docs
			.andDo(restDocs.document(
				responseFields(
					fieldWithPath("data.id").type(NUMBER).description("유저 ID"),
					fieldWithPath("data.nickname").type(STRING).description("닉네임"),
					fieldWithPath("data.profileImgUrl").type(STRING).description("프로필 이미지"),
					fieldWithPath("data.introduction").type(STRING).description("한줄 소개"),
					fieldWithPath("data.leaderCount").type(NUMBER).description("방장 횟수"),
					fieldWithPath("data.leaderCount").type(NUMBER).description("방장 횟수"),
					fieldWithPath("data.crewCount").type(NUMBER).description("모임 참여 횟수"),
					fieldWithPath("data.tasteScore").type(NUMBER).description("맛잘알 점수"),
					fieldWithPath("data.mannerScore").type(NUMBER).description("매너 온도")

				))
			);
	}

	@Test
	@DisplayName("[성공] 유저 아이디로 유저 정보를 조회할 수 있다.")
	void getUserProfileSuccessTest() throws Exception {
		// when
		mockMvc.perform(RestDocumentationRequestBuilders
				.get("/api/v1/user/{userId}", savedUserId)
				.header(HttpHeaders.AUTHORIZATION, BEARER_TYPE + ACCESS_TOKEN)
			)
			// then
			.andExpectAll(
				handler().methodName("getUserProfile"),
				status().isOk())
			// docs
			.andDo(restDocs.document(
				responseFields(
					fieldWithPath("data.id").type(NUMBER).description("유저 ID"),
					fieldWithPath("data.nickname").type(STRING).description("닉네임"),
					fieldWithPath("data.profileImgUrl").type(STRING).description("프로필 이미지"),
					fieldWithPath("data.introduction").type(STRING).description("한줄 소개"),
					fieldWithPath("data.leaderCount").type(NUMBER).description("방장 횟수"),
					fieldWithPath("data.leaderCount").type(NUMBER).description("방장 횟수"),
					fieldWithPath("data.crewCount").type(NUMBER).description("모임 참여 횟수"),
					fieldWithPath("data.tasteScore").type(NUMBER).description("맛잘알 점수"),
					fieldWithPath("data.mannerScore").type(NUMBER).description("매너 온도")
				))
			);
	}

	@Test
	@DisplayName("[성공] 자신의 프로필을 수정할 수 있다.")
	void updateMyProfileSuccessTest() throws Exception {
		UpdateUserRequest request = getUpdateUserRequest();

		// when
		mockMvc.perform(RestDocumentationRequestBuilders.put("/api/v1/user/me")
				.header(HttpHeaders.AUTHORIZATION, BEARER_TYPE + ACCESS_TOKEN)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request))
			)
			// then
			.andExpectAll(
				handler().methodName("updateMyProfile"),
				status().isOk())
			// docs
			.andDo(restDocs.document(
				responseFields(
					fieldWithPath("data.id").type(NUMBER).description("유저 ID"),
					fieldWithPath("data.nickname").type(STRING).description("닉네임"),
					fieldWithPath("data.profileImgUrl").type(STRING).description("프로필 이미지"),
					fieldWithPath("data.introduction").type(STRING).description("한줄 소개"),
					fieldWithPath("data.leaderCount").type(NUMBER).description("방장 횟수"),
					fieldWithPath("data.leaderCount").type(NUMBER).description("방장 횟수"),
					fieldWithPath("data.crewCount").type(NUMBER).description("모임 참여 횟수"),
					fieldWithPath("data.tasteScore").type(NUMBER).description("맛잘알 점수"),
					fieldWithPath("data.mannerScore").type(NUMBER).description("매너 온도")
				))
			);

	}

	@Test
	@DisplayName("자신의 계정을 삭제할 수 있다")
	void deleteMyProfileSuccessTest() throws Exception {

		//given
		User createdUser = User.builder()
			.nickname(DEFAULT_NICKNAME)
			.profileImgUrl(DEFAULT_PROFILE_IMG_URL)
			.provider(PROVIDER_KAKAO)
			.oauthId(OAUTH_ID)
			.build();

		User user = userRepository.save(createdUser);
		Long userId = user.getId();
		String token = jwtTokenProvider.createAccessToken(userId, "USER");

		// when
		mockMvc.perform(RestDocumentationRequestBuilders.delete("/api/v1/user/me")
				.header(HttpHeaders.AUTHORIZATION, BEARER_TYPE + token)
			)
			// then
			.andExpectAll(
				handler().methodName("deleteMyProfile"),
				status().isNoContent()
			);
	}
}
