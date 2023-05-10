package com.prgrms.mukvengers.global.security.token.api;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import javax.servlet.http.Cookie;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;

import com.prgrms.mukvengers.base.ControllerTest;
import com.prgrms.mukvengers.global.config.EmbeddedRedisConfig;
import com.prgrms.mukvengers.global.security.token.service.TokenService;

@Import(EmbeddedRedisConfig.class)
class TokenControllerTest extends ControllerTest {

	private final String invalidRefreshToken = "invalidRefreshToken";

	private String validRefreshToken;

	@Autowired
	private TokenService tokenService;

	@BeforeEach
	void setUp() {
		validRefreshToken = tokenService.createRefreshToken(1L, "role");
	}

	@Test
	@DisplayName("[성공] refresh token으로 access token 재발급")
	void refreshAccessTokenTest_success() throws Exception {
		mockMvc.perform(post("/api/v1/tokens")
				.cookie(new Cookie("refreshToken", validRefreshToken)))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
			.andExpect(jsonPath("$.accessToken").exists())
			.andDo(print());
	}

	@Test
	@DisplayName("[실패] 재발급 실패 : refresh token이 올바르지 않은 경우 - 401 : A004 유효하지 않은 리프레쉬 토큰입니다.")
	void refreshAccessTokenTest_fail_invalid() throws Exception {
		mockMvc.perform(post("/api/v1/tokens")
				.cookie(new Cookie("refreshToken", invalidRefreshToken)))
			.andExpect(status().is4xxClientError())
			.andDo(print());
	}

	@Test
	@DisplayName("[실패] 재발급 실패 : refresh token이 없는 경우 - 400 : C003 인자 부족")
	void refreshAccessTokenTest_fail_notfound() throws Exception {
		mockMvc.perform(post("/api/v1/tokens")
				.cookie(new Cookie("null", "")))
			.andExpect(status().is4xxClientError())
			.andDo(print());
	}

	@Test
	@DisplayName("[성공] 로그 아웃 - refresh token 삭제")
	void expireRefreshTokenTes_success() throws Exception { // 존재하지 않더라도 실패 X
		mockMvc.perform(delete("/api/v1/tokens")
				.cookie(new Cookie("refreshToken", validRefreshToken)))
			.andExpect(status().isNoContent())
			.andExpect(header().string("Set-Cookie",
				"refreshToken=; Path=/; Max-Age=0; Expires=Thu, 1 Jan 1970 00:00:00 GMT; Secure; HttpOnly; SameSite=None"))
			.andDo(print());
	}
}