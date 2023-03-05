package com.prgrms.mukvengers.global.security.jwt;

import static com.prgrms.mukvengers.utils.UserObjectProvider.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

import javax.servlet.ServletException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;

import com.prgrms.mukvengers.global.security.token.exception.ExpiredTokenException;
import com.prgrms.mukvengers.global.security.token.exception.InvalidTokenException;
import com.prgrms.mukvengers.global.utils.ExtractUtil;

class JwtAuthenticationFilterTest {

	private static final String BEARER_TYPE = "Bearer ";
	private static final String ISSUER = "issuer";
	private static final String SECRET_KEY = "kkini-team-kkini-project-fighting";
	private static final long ACCESS_TOKEN_EXPIRY_SECONDS = 3L;
	private static final long REFRESH_TOKEN_EXPIRY_SECONDS = 5L;
	private static final String USER_ROLE = "USER";

	private JwtAuthenticationFilter jwtAuthenticationFilter;
	private JwtTokenProvider jwtTokenProvider;
	private MockHttpServletRequest request;
	private MockHttpServletResponse response;
	private MockFilterChain filterChain;
	private String ACCESS_TOKEN;

	@BeforeEach
	void setUp() {
		request = new MockHttpServletRequest();
		response = new MockHttpServletResponse();
		filterChain = new MockFilterChain();
		jwtTokenProvider = new JwtTokenProvider(ISSUER, SECRET_KEY, ACCESS_TOKEN_EXPIRY_SECONDS,
			REFRESH_TOKEN_EXPIRY_SECONDS);
		jwtAuthenticationFilter = new JwtAuthenticationFilter(jwtTokenProvider);
		ACCESS_TOKEN = jwtTokenProvider.createAccessToken(USER_ID, USER_ROLE);
	}

	@Test
	@DisplayName("[성공] 헤더(Authorization Bearer)에서 토큰을 추출할 수 있다.")
	void extractTokenFromRequest_success() {
		// given
		request.addHeader(HttpHeaders.AUTHORIZATION, BEARER_TYPE + ACCESS_TOKEN);
		// when
		String token = ExtractUtil.extractTokenFromRequest(request);
		// then
		assertDoesNotThrow(() -> jwtTokenProvider.validateToken(token));
	}

	@Test
	@DisplayName("[성공] 유효한 Access Token이 헤더에 있는 경우 인증된 사용자 정보(userId, token)가 SecurityContextHolder에 저장된다.")
	void doFilterInternal_success() throws ServletException, IOException {
		// given
		request.addHeader(HttpHeaders.AUTHORIZATION, BEARER_TYPE + ACCESS_TOKEN);
		// when
		jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);
		// then
		// 현재 SecurityContext에 들어 있는 인증된 사용자(principal) 추출
		JwtAuthentication user = (JwtAuthentication)
			SecurityContextHolder
				.getContext()
				.getAuthentication()
				.getPrincipal();

		assertThat(user)
			.hasFieldOrPropertyWithValue("id", USER_ID)
			.hasFieldOrPropertyWithValue("accessToken", ACCESS_TOKEN);
	}

	// 띄어쓰기를 어떻게 하면 좋을까?

	@Test
	@DisplayName("[실패] 유효한 Access Token이 헤더에 있는 경우")
	void doFilterInternal_InvalidToken_fail() {
		// given
		String invalidToken = "invalidToken";

		request.addHeader(HttpHeaders.AUTHORIZATION, BEARER_TYPE + invalidToken);

		// when & then
		assertThatThrownBy(
			() -> jwtAuthenticationFilter.doFilterInternal(request, response, filterChain))
			.isInstanceOf(InvalidTokenException.class);

	}

	@Test
	@DisplayName("[실패] 만료된 토큰일 경우 예외를 발생한다.")
	void doFilterInternal_ExpiredToken_fail() {
		// given
		JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(ISSUER, SECRET_KEY, 0, 3);

		String newToken = jwtTokenProvider.createAccessToken(USER_ID, USER_ROLE);

		request.addHeader(HttpHeaders.AUTHORIZATION, BEARER_TYPE + newToken);

		// when & then
		assertThatThrownBy(
			() -> jwtAuthenticationFilter.doFilterInternal(request, response, filterChain))
			.isInstanceOf(ExpiredTokenException.class);
	}

}
