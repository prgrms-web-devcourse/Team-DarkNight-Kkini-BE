package com.prgrms.mukvengers.global.security.token.api;

import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import javax.servlet.http.Cookie;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import com.prgrms.mukvengers.base.ControllerTest;
import com.prgrms.mukvengers.global.security.token.exception.RefreshTokenNotFoundException;
import com.prgrms.mukvengers.global.security.token.service.TokenService;

class TokenControllerTest extends ControllerTest {

	private final String invalidRefreshToken = "invalidRefreshToken";
	private final String validRefreshToken = "validRefreshToken";
	@MockBean
	private TokenService tokenservice;

	@BeforeEach
	void setUp() {
		given(tokenservice.getAccessTokensByRefreshToken(any(String.class))).willReturn(validRefreshToken);
	}

	@Test
	@DisplayName("[성공] refresh token으로 access token 재발급")
	void refreshAccessTokenTest_success() throws Exception {
		given(tokenservice.getAccessTokensByRefreshToken(any(String.class))).willReturn(validRefreshToken);
		mockMvc.perform(post("/api/v1/tokens")
				.cookie(new Cookie("refreshToken", validRefreshToken)))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
			.andExpect(jsonPath("$.accessToken").exists())
			.andDo(print());
	}

	@Test
	@DisplayName("[실패] 재발급 실패 : refresh token이 올바르지 않은 경우 - 401")
	void refreshAccessTokenTest_fai_invalid() throws Exception {
		willThrow(RefreshTokenNotFoundException.class).given(
			tokenservice.getAccessTokensByRefreshToken(any(String.class)));
		mockMvc.perform(post("/api/v1/tokens")
				.cookie(new Cookie("refreshToken", invalidRefreshToken)))
			.andExpect(status().is4xxClientError())
			.andDo(print());
	}

	@Test
	@DisplayName("[실패] 재발급 실패 : refresh token이 없는 경우 - 401")
	void refreshAccessTokenTest_fail_notfound() throws Exception {
		mockMvc.perform(post("/api/v1/tokens")
				.cookie(new Cookie("null", "")))
			.andExpect(status().is4xxClientError())
			.andDo(print());
	}

	@Test
	@DisplayName("[성공] 로그 아웃 - refresh token 삭제")
	void expireRefreshTokenTes_success() throws Exception { // 존재하지 않더라도 실패 X
		willDoNothing().given(tokenservice).deleteRefreshToken(any(String.class));
		mockMvc.perform(delete("/api/v1/tokens")
				.cookie(new Cookie("refreshToken", validRefreshToken)))
			.andExpect(status().isNoContent())
			.andExpect(header().string("Set-Cookie",
				"refreshToken=; Path=/; Max-Age=0; Expires=Thu, 1 Jan 1970 00:00:00 GMT; Secure; HttpOnly; SameSite=None"))
			.andDo(print());
	}
}