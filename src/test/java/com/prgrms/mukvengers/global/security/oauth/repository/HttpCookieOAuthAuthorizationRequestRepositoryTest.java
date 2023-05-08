package com.prgrms.mukvengers.global.security.oauth.repository;

import static com.prgrms.mukvengers.global.security.oauth.repository.HttpCookieOAuthAuthorizationRequestRepository.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;

import com.prgrms.mukvengers.base.SliceTest;
import com.prgrms.mukvengers.global.utils.CookieUtil;

@Import(CookieUtil.class)
class HttpCookieOAuthAuthorizationRequestRepositoryTest extends SliceTest {

	private final HttpCookieOAuthAuthorizationRequestRepository repository
		= new HttpCookieOAuthAuthorizationRequestRepository();

	@Mock
	private OAuth2AuthorizationRequest authorizationRequest;

	@Mock
	private HttpServletRequest request;

	@Mock
	private HttpServletResponse response;

	@Test
	@DisplayName("[성공] authorizationRequest를 쿠키에 저장하고 쿠키에서 가져올 수 있다.")
	void saveAndLoadAuthorizationRequestTest_success() {

		Cookie cookie = getAuthorizationRequestCookie();
		given(request.getCookies()).willReturn(new Cookie[] {cookie});

		//when
		repository.saveAuthorizationRequest(authorizationRequest, request, response);
		OAuth2AuthorizationRequest loadedAuthorizationRequest = repository.loadAuthorizationRequest(request);

		//then
		then(response).should().addCookie(any(Cookie.class));
		assertThat(loadedAuthorizationRequest).isInstanceOf(OAuth2AuthorizationRequest.class);
	}

	@Test
	@DisplayName("[성공] 쿠키에 포함된 authorizationRequest를 삭제한다.(빈 쿠키로 덮어쓴다)")
	void removeAuthorizationRequestCookies_success() {

		//given
		Cookie cookie = getAuthorizationRequestCookie();
		given(request.getCookies()).willReturn(new Cookie[] {cookie});

		repository.saveAuthorizationRequest(authorizationRequest, request, response);

		//when
		repository.removeAuthorizationRequestCookies(request, response);

		//then
		then(response).should(times(2)).addCookie(any(Cookie.class));
	}

	private Cookie getAuthorizationRequestCookie() {
		return new Cookie(OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME,
			CookieUtil.serialize(authorizationRequest));
	}
}