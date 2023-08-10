package com.prgrms.mukvengers.global.auth.oauth.handler;

import static com.prgrms.mukvengers.global.auth.oauth.repository.HttpCookieOAuthAuthorizationRequestRepository.*;
import static java.nio.charset.StandardCharsets.*;
import static org.mockito.BDDMockito.*;

import java.net.URLEncoder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.web.util.UriComponentsBuilder;

import com.prgrms.mukvengers.base.SliceTest;
import com.prgrms.mukvengers.global.auth.oauth.dto.AuthUserInfo;
import com.prgrms.mukvengers.global.auth.oauth.dto.CustomOAuth2User;
import com.prgrms.mukvengers.global.auth.token.dto.Tokens;
import com.prgrms.mukvengers.global.auth.token.service.TokenService;

class OAuthAuthenticationSuccessHandlerTest extends SliceTest {

	@Mock
	private TokenService tokenService;

	@InjectMocks
	private OAuthAuthenticationSuccessHandler oAuthAuthenticationSuccessHandler;

	@Mock
	private HttpServletRequest request;

	@Mock
	private HttpServletResponse response;

	@Mock
	private Authentication authentication;

	@Mock
	private CustomOAuth2User customOAuth2User;

	@Test
	@DisplayName("[성공] 소셜 로그인 성공시 accessToken은 쿼리 파라미터로, refreshToken은 쿠키에 포함해 redirectUri 주소로 리다이렉트 한다.")
	void OnAuthenticationSuccessTest_success() throws Exception {

		// Given
		String accessToken = "testAccessToken";
		String refreshToken = "testRefreshToken";
		String redirectUri = "https://kkini.com/callback?restaurantName=테스트&test=query"; // 한글 쿼리 파라미터
		Tokens tokens = new Tokens(accessToken, refreshToken);
		AuthUserInfo authUserInfo = new AuthUserInfo(1L, "test", "USER");
		Cookie cookie = new Cookie(REDIRECT_URI_PARAM_COOKIE_NAME, redirectUri);
		ResponseCookie responseCookie = getResponseCookie(refreshToken);

		given(authentication.getPrincipal()).willReturn(customOAuth2User);
		given(customOAuth2User.getUserInfo()).willReturn(authUserInfo);
		given(tokenService.createTokens(authUserInfo)).willReturn(tokens);
		given(tokenService.getRefreshTokenExpirySeconds()).willReturn(1);
		given(request.getCookies()).willReturn(new Cookie[] {cookie});

		String encodedQuery = URLEncoder.encode("테스트", UTF_8);
		String afterEncodingUrl =
			"https://kkini.com/callback?restaurantName=" + encodedQuery + "&test=query";

		String expectedTargetUrl = UriComponentsBuilder.fromUriString(afterEncodingUrl)
			.queryParam("accessToken", accessToken)
			.build().toUriString();

		// When
		oAuthAuthenticationSuccessHandler.onAuthenticationSuccess(request, response, authentication);

		// Then
		then(response).should().addHeader(eq("Set-Cookie"), eq(responseCookie.toString()));
		then(response).should().encodeRedirectURL(expectedTargetUrl);
	}

	private ResponseCookie getResponseCookie(String refreshToken) {
		return ResponseCookie.from("refreshToken", refreshToken)
			.path("/")
			.httpOnly(true)
			.sameSite("None")
			.secure(true)
			.maxAge(1L)
			.build();
	}
}