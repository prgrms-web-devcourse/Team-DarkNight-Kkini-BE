package com.prgrms.mukvengers.domain.user.service;

import static com.prgrms.mukvengers.utils.UserObjectProvider.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.prgrms.mukvengers.base.ServiceTest;
import com.prgrms.mukvengers.domain.user.dto.request.UpdateUserRequest;
import com.prgrms.mukvengers.domain.user.dto.response.UserProfileResponse;
import com.prgrms.mukvengers.domain.user.exception.UserNotFoundException;
import com.prgrms.mukvengers.domain.user.mapper.UserMapper;
import com.prgrms.mukvengers.domain.user.model.User;

class UserServiceTest extends ServiceTest {

	@Autowired
	private UserMapper userMapper;

	@Nested
	@DisplayName("#3 유저 CRUD 기능")
	class CRUDTest {

		User savedUser;

		@BeforeEach
		void setUp() {
			savedUser = userRepository.save(createUser());
		}

		@Test
		@DisplayName("[성공] userId를 통해서 사용자 프로필를 조회할 수 있다.")
		void getUserProfile_success() {
			// given & when
			UserProfileResponse userProfile = userService.getUserProfile(savedUser.getId());
			//then
			assertThat(userProfile)
				.isEqualTo(userMapper.toSingleUserResponse(savedUser));
		}

		@Test
		@DisplayName("[성공] 사용자의 프로필(닉네임, 프로필 이미지, 자기소개)를 한번에 수정할 수 있다.")
		void updateUserProfile_success() {
			//given
			UpdateUserRequest request = new UpdateUserRequest("테스트2", "https://updateImg.jpg", "수정된 자기소개");
			// when
			UserProfileResponse userProfile = userService.updateUserProfile(request, savedUser.getId());
			// then
			assertThat(userProfile)
				.hasFieldOrPropertyWithValue("id", savedUser.getId())
				.hasFieldOrPropertyWithValue("nickname", request.nickName())
				.hasFieldOrPropertyWithValue("profileImgUrl", request.profileImgUrl())
				.hasFieldOrPropertyWithValue("introduction", request.introduction())
				.hasFieldOrPropertyWithValue("leaderCount", 0)
				.hasFieldOrPropertyWithValue("crewCount", 0)
				.hasFieldOrPropertyWithValue("tasteScore", 0)
				.hasFieldOrPropertyWithValue("mannerScore", 36.5)
			;
		}

		@Test
		@DisplayName("[성공] userId를 통해서 사용자 정보를 삭제할 수 있다.")
		void deleteUser_success() {
			assertDoesNotThrow(
				() -> userService.deleteUser(savedUser.getId())
			);
			// 삭제 후 조회
			assertThatThrownBy(() -> userService.getUserProfile(savedUser.getId()))
				.isInstanceOf(UserNotFoundException.class);
		}

		@Test
		@DisplayName("[실패] userId에 해당하는 사용자가 없으면 UserNotFoundException")
		void notFoundMatchUser_fail() {
			Long UNSAVED_USER_ID = 0L;
			assertAll(
				() -> assertThatThrownBy(() -> userService.getUserProfile(UNSAVED_USER_ID))
					.isInstanceOf(UserNotFoundException.class),
				() -> assertThatThrownBy(() -> userService.updateUserProfile(getUpdateUserRequest(), UNSAVED_USER_ID))
					.isInstanceOf(UserNotFoundException.class),
				() -> assertThatThrownBy(() -> userService.deleteUser(UNSAVED_USER_ID))
					.isInstanceOf(UserNotFoundException.class)
			);
		}
	}

}
