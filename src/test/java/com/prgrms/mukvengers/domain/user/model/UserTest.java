package com.prgrms.mukvengers.domain.user.model;

import static org.assertj.core.api.Assertions.*;

import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;

class UserTest {

	private static final String SIZE_255_STRING_DUMMY = "0" + "1234567890".repeat(26);

	private static Stream<Arguments> provideStringDummy() {
		return Stream.of(
			Arguments.of("  ", true),
			Arguments.of(SIZE_255_STRING_DUMMY, true)
		);
	}

	@Test
	@DisplayName("[성공] 닉네임, 프로필 이미지, provider, oauthId로 유저를 생성할 수 있다.")
	void create_success() {
		//given & when
		User user = User.builder()
			.nickname("테스트")
			.profileImgUrl("https://defaultImg.jpg")
			.provider("kakao")
			.oauthId("12345")
			.build();
		//then
		// 나머지 값들은 디폴트 값으로 자동 생성됨
		assertThat(user)
			.hasFieldOrPropertyWithValue("nickname", "테스트")
			.hasFieldOrPropertyWithValue("profileImgUrl", "https://defaultImg.jpg")
			.hasFieldOrPropertyWithValue("introduction", "자기소개를 작성해주세요")
			.hasFieldOrPropertyWithValue("leaderCount", 0)
			.hasFieldOrPropertyWithValue("crewCount", 0)
			.hasFieldOrPropertyWithValue("tasteScore", 0)
			.hasFieldOrPropertyWithValue("mannerScore", 36.5)
			.hasFieldOrPropertyWithValue("provider", "kakao")
			.hasFieldOrPropertyWithValue("oauthId", "12345")
			.hasFieldOrPropertyWithValue("reportedCount", 0)
			.hasFieldOrPropertyWithValue("enabled", false);
	}

	@ParameterizedTest
	@NullAndEmptySource
	@MethodSource("provideStringDummy")
	@DisplayName("[성공] nickName 필드는 유효성 검증을 한다. 따라서, null, 빈 값, 255이상의 값이 들어올 수 없다.")
	void validateNickname_success(String inputNickName) {
		//given & when & then
		assertThatThrownBy(() -> User.builder().nickname(inputNickName).build())
			.isInstanceOf(IllegalArgumentException.class);
	}

}
