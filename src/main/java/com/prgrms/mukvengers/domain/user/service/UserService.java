package com.prgrms.mukvengers.domain.user.service;

import com.prgrms.mukvengers.domain.user.dto.request.UpdateUserRequest;
import com.prgrms.mukvengers.domain.user.dto.response.UserProfileResponse;

public interface UserService {

	/* 회원 프로필 조회 */
	UserProfileResponse getUserProfile(Long userId);

	/* 회원 프로필 수정 */
	UserProfileResponse updateUserProfile(UpdateUserRequest updateUserRequest, Long userId);

	/* 회원 탈퇴 */
	void deleteUser(Long userId);

}
