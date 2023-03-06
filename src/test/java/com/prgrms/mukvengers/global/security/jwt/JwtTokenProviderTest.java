package com.prgrms.mukvengers.global.security.jwt;

import static com.prgrms.mukvengers.utils.UserObjectProvider.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.prgrms.mukvengers.global.security.token.exception.ExpiredTokenException;
import com.prgrms.mukvengers.global.security.token.exception.InvalidTokenException;

import io.jsonwebtoken.Claims;

class JwtTokenProviderTest {

	private static final String ISSUER = "issuer";
	private static final String SECRET_KEY = "kkini-team-kkini-project-fighting";
	private static final int ACCESS_TOKEN_EXPIRY_SECONDS = 3;
	private static final String USER_ROLE = "USER";

	private final JwtTokenProvider jwtTokenProvider
		= new JwtTokenProvider(ISSUER, SECRET_KEY, ACCESS_TOKEN_EXPIRY_SECONDS);

	private String accessToken;

	@Test
	@DisplayName("[성공] 페이로드(유저ID, ROLE)를 담은 JWT를 생성할 수 있다.")
	void createAccessToken_success() {
		// given : 유저 아이디와 권한에 대해 100% 유효함을 보장하지는 못한다.
		Long userId = 0L;

		String role = "ROLE";
		// when & then
		assertDoesNotThrow(() -> jwtTokenProvider.createAccessToken(userId, role));
	}

	@Test
	@DisplayName("[성공] JWT에서 페이로드(유저ID, ROLE)를 추출할 수 있다.")
	void getClaims_success() {
		// given
		accessToken = jwtTokenProvider.createAccessToken(USER_ID, USER_ROLE);
		// when
		Claims claims = jwtTokenProvider.getClaims(accessToken);
		// then
		assertAll(
			() -> assertThat(claims.get("userId", Long.class)).isEqualTo(USER_ID),
			() -> assertThat(claims.get("role", String.class)).isEqualTo(USER_ROLE)
		);
	}

	@Test
	@DisplayName("[성공] 토큰 값을 유효성 검사할 수 있다. 유효한 토큰의 경우 예외를 발생시키지 않는다.")
	void validateToken_success() {
		// given
		accessToken = jwtTokenProvider.createAccessToken(1L, "role");
		// when & then
		assertDoesNotThrow(() -> jwtTokenProvider.validateToken(accessToken));
	}

	@Test
	@DisplayName("[실패] 토큰의 만료 시간이 지나면 ExpiredTokenException 예외를 발생한다.")
	void validateToken_fail_byExpired() throws Exception {
		// given
		accessToken = jwtTokenProvider.createAccessToken(1L, "role");

		Thread.sleep(ACCESS_TOKEN_EXPIRY_SECONDS * 1000L);

		// when & then
		assertThatThrownBy(() -> jwtTokenProvider.validateToken(accessToken))
			.isInstanceOf(ExpiredTokenException.class);
	}

	@Test
	@DisplayName("[실패] 유효하지 않은 토큰 검증시 InvalidTokenException 예외를 발생한다.")
	void validateToken_fail_byInvalid() {
		// given
		accessToken = "InvalidToken";
		// when & then
		assertThatThrownBy(() -> jwtTokenProvider.validateToken(accessToken))
			.isInstanceOf(InvalidTokenException.class);
	}

	@Test
	@DisplayName("[성공] 토큰 값이 null인 경우 InvalidTokenException이 발생한다.")
	void validateToken_fail_byNull() {
		// given
		accessToken = null;
		// when & then
		assertThatThrownBy(() -> jwtTokenProvider.validateToken(accessToken))
			.isInstanceOf(InvalidTokenException.class);
	}

	@Test
	@DisplayName("[성공] 발행자 정보가 달라도 예외가 발생하지 않는다.????")
	void validateToken_failByWrongIssuer() {
		// given
		String invalidIssuer = "wrong-issuer";

		JwtTokenProvider wongTokenProvider
			= new JwtTokenProvider(invalidIssuer, SECRET_KEY, ACCESS_TOKEN_EXPIRY_SECONDS);
		//when
		accessToken = wongTokenProvider.createAccessToken(USER_ID, USER_ROLE);
		//then
		assertDoesNotThrow(() -> jwtTokenProvider.validateToken(accessToken));
		assertDoesNotThrow(() -> jwtTokenProvider.getClaims(accessToken));
	}

	@Test
	@DisplayName("[실패] 올바르지 않은 시그니처(시크릿 키)로 검증 시 예외를 발생한다.")
	void validateToken_fail_byWrongSign() {
		// given
		String invalidSecretKey = "when_the_night_comes,we_get_stronger";

		JwtTokenProvider wongTokenProvider
			= new JwtTokenProvider(ISSUER, invalidSecretKey, ACCESS_TOKEN_EXPIRY_SECONDS);
		//when
		accessToken = wongTokenProvider.createAccessToken(USER_ID, USER_ROLE);
		//then
		assertThatThrownBy(() -> jwtTokenProvider.validateToken(accessToken))
			.isInstanceOf(InvalidTokenException.class);
	}

}
