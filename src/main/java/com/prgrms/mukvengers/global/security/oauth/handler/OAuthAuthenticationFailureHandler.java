package com.prgrms.mukvengers.global.security.oauth.handler;

import static com.prgrms.mukvengers.global.security.oauth.repository.HttpCookieOAuthAuthorizationRequestRepository.*;
import static java.nio.charset.StandardCharsets.*;

import java.io.IOException;
import java.net.URLDecoder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import com.prgrms.mukvengers.global.security.oauth.repository.HttpCookieOAuthAuthorizationRequestRepository;
import com.prgrms.mukvengers.global.utils.CookieUtil;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OAuthAuthenticationFailureHandler
	extends SimpleUrlAuthenticationFailureHandler {

	private static final String DEFAULT_TARGET_URL = "/";
	private final HttpCookieOAuthAuthorizationRequestRepository httpCookieOAuthAuthorizationRequestRepository;

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
		AuthenticationException exception) throws IOException {
		String redirectUrl = CookieUtil.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
			.map(Cookie::getValue)
			.map(cookie -> URLDecoder.decode(cookie, UTF_8))
			.orElse(DEFAULT_TARGET_URL);

		String targetUrl = UriComponentsBuilder.fromUriString(redirectUrl)
			.queryParam("error", exception.getMessage()) // TODO: exception.getMessage() -> error code
			.build().toUriString();

		httpCookieOAuthAuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);

		getRedirectStrategy().sendRedirect(request, response, targetUrl);
	}
}


