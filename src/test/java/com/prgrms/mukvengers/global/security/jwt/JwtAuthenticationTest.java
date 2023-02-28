package com.prgrms.mukvengers.global.security.jwt;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import com.prgrms.mukvengers.global.security.token.exception.InvalidTokenException;

class JwtAuthenticationTest {

	private static final String ACCESS_TOKEN = "validToken";
	private static final long USER_ID = 1L;

	@Test
	@DisplayName("[성공] token과 유저ID로 인증된 사용자(principal)를 의미하는 jwtAuthentication을 생성할 수 있다.")
	void create_success() {
		// given
		JwtAuthentication jwtAuthentication
			= new JwtAuthentication(USER_ID, ACCESS_TOKEN);
		// when & then
		assertThat(jwtAuthentication)
			.hasFieldOrPropertyWithValue("id", USER_ID)
			.hasFieldOrPropertyWithValue("accessToken", ACCESS_TOKEN);

	}

	@ParameterizedTest
	@NullSource
	@ValueSource(longs = {0L, -1L, -100L})
	@DisplayName("[실패] userId는 null 혹은 0 이하일 수 없다.")
	void create_fail_byInvalidUserId(Long inputUserId) {
		//given & when & then
		assertThatThrownBy(() -> new JwtAuthentication(inputUserId, ACCESS_TOKEN))
			.isInstanceOf(InvalidTokenException.class);
	}

	@ParameterizedTest
	@NullAndEmptySource
	@DisplayName("[실패] token은 null 혹은 빈 값일 수 없다.")
	void create_fail_byInvalidToken(String inputToken) {
		//given & when & then
		assertThatThrownBy(() -> new JwtAuthentication(USER_ID, inputToken))
			.isInstanceOf(InvalidTokenException.class);
	}

}