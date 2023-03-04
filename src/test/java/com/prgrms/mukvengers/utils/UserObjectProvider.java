package com.prgrms.mukvengers.utils;

import com.prgrms.mukvengers.domain.user.dto.request.UpdateUserRequest;
import com.prgrms.mukvengers.domain.user.model.User;

public class UserObjectProvider {

	public static final Long USER_ID = 1L;
	public static final String DEFAULT_NICKNAME = "끼니";
	public static final String DEFAULT_PROFILE_IMG_URL = "https://defaultImg.jpg";
	public static final String PROVIDER_KAKAO = "kakao";
	public static final String OAUTH_ID = "12345";

	public static User createUser() {
		return User.builder()
			.nickname(DEFAULT_NICKNAME)
			.profileImgUrl(DEFAULT_PROFILE_IMG_URL)
			.provider(PROVIDER_KAKAO)
			.oauthId(OAUTH_ID)
			.build();
	}

	public static User createUser(String oauth_id) {
		return User.builder()
			.nickname(DEFAULT_NICKNAME)
			.profileImgUrl(DEFAULT_PROFILE_IMG_URL)
			.provider(PROVIDER_KAKAO)
			.oauthId(oauth_id)
			.build();
	}

	public static UpdateUserRequest getUpdateUserRequest() {
		return new UpdateUserRequest(
			"수정된 이름",
			"https://updatedImg.jpg",
			"수정돤 자기소개"
		);
	}

}
