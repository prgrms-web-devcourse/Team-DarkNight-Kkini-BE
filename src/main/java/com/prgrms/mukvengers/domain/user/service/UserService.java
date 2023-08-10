package com.prgrms.mukvengers.domain.user.service;

import com.prgrms.mukvengers.domain.user.dto.request.UpdateUserRequest;
import com.prgrms.mukvengers.domain.user.dto.response.UserProfileResponse;
import com.prgrms.mukvengers.global.auth.oauth.dto.AuthUserInfo;
import com.prgrms.mukvengers.global.auth.oauth.dto.OAuthUserInfo;

public interface UserService {

	/* 회원 등록 */
	AuthUserInfo getOrRegisterUser(OAuthUserInfo oauthUserInfo);

	/* 회원 프로필 조회 */
	UserProfileResponse getUserProfile(Long userId);

	/* 회원 프로필 수정 */
	UserProfileResponse updateUserProfile(UpdateUserRequest updateUserRequest, Long userId);

	/* 회원 탈퇴 */
	void deleteUser(Long userId, String refreshToken);

}
