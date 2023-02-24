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

	private static final String email = "test@test.com";
	private static final String nickName = "테스터";
	private static final String introduce = "테스트하는 중";
	private static final String prologName = "테스터의 prolog";
	private static final String provider = "kakao";
	private static final String oauthId = "1";
	private static final String SIZE_255_STRING_DUMMY = "0" + "1234567890".repeat(26);

	private static Stream<Arguments> provideStringDummy() {
		return Stream.of(
			Arguments.of("  ", true),
			Arguments.of(SIZE_255_STRING_DUMMY, true)
		);
	}

	@Test
	@DisplayName("[성공] 유저 생성")
	void createTest() {
		//given & when
		User user = User.builder()
			.nickname("테스트")
			.profileImgUrl("https://defaultImg.jpg")
			.provider("kakao")
			.oauthId("12345")
			.build();
		//then
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
	@DisplayName("nickName 유효성 검증")
	@NullAndEmptySource
	@MethodSource("provideStringDummy")
	void nickNameValidateTest(String inputNickName) {
		//given & when & then
		assertThatThrownBy(() -> getBuilder().nickname(inputNickName).build())
			.isInstanceOf(IllegalArgumentException.class);
	}

	@ParameterizedTest
	@DisplayName("자기 소개 유효성 검증")
	@NullAndEmptySource
	@MethodSource("provideStringDummy")
	void emailValidateTest(String inputIntroduce) {
		//given & when & then
		assertThatThrownBy(() -> getBuilder().nickname(inputIntroduce).build())
			.isInstanceOf(IllegalArgumentException.class);
	}

	private User.UserBuilder getBuilder() {
		return User.builder();
	}

}
