package com.prgrms.mukvengers.global.security.oauth.handler;

import static com.prgrms.mukvengers.global.security.oauth.repository.HttpCookieOAuthAuthorizationRequestRepository.*;
import static org.mockito.BDDMockito.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.core.AuthenticationException;

import com.prgrms.mukvengers.base.SliceTest;
import com.prgrms.mukvengers.global.security.oauth.repository.HttpCookieOAuthAuthorizationRequestRepository;

class OAuthAuthenticationFailureHandlerTest extends SliceTest {

	@Mock
	private HttpCookieOAuthAuthorizationRequestRepository repository;

	@InjectMocks
	private OAuthAuthenticationFailureHandler oAuthAuthenticationFailureHandler;

	@Mock
	private HttpServletRequest request;

	@Mock
	private HttpServletResponse response;

	@Mock
	private AuthenticationException exception;

	@Test
	@DisplayName("[성공] 소셜 로그인 실패시 'error' 쿼리에 에러 메시지 파라미터를 포함한 후에 쿠키에 포함된 redirectUri 주소로 리다이렉트 한다.")
	void onAuthenticationFailure_WhenRedirectUrlCookieIsPresent() throws Exception {
		Cookie redirectUriCookie = new Cookie(REDIRECT_URI_PARAM_COOKIE_NAME, "https://kkini.com/");
		given(request.getCookies()).willReturn(new Cookie[] {redirectUriCookie});
		given(exception.getMessage()).willReturn("test");

		oAuthAuthenticationFailureHandler.onAuthenticationFailure(request, response, exception);

		then(repository).should().removeAuthorizationRequestCookies(request, response);
		then(response).should().encodeRedirectURL("https://kkini.com/?error=" + exception.getMessage());
	}

	@Test
	@DisplayName("[성공] 쿠키에 redirectUri 주소가 없는 경우 서버 주소(디폴트 주소)로 리다이렉트 한다.")
	void onAuthenticationFailure_WhenRedirectUrlCookieIsNotPresent() throws Exception {
		given(request.getCookies()).willReturn(null);
		given(request.getContextPath()).willReturn("https://server.com");
		given(exception.getMessage()).willReturn("test");

		oAuthAuthenticationFailureHandler.onAuthenticationFailure(request, response, exception);

		then(repository).should().removeAuthorizationRequestCookies(request, response);
		then(response).should().encodeRedirectURL("https://server.com/?error=" + exception.getMessage());
	}
}
