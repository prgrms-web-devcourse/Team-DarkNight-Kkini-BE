package com.prgrms.mukvengers.global.security.oauth.handler;

import static com.prgrms.mukvengers.global.security.oauth.handler.HttpCookieOAuthAuthorizationRequestRepository.*;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import com.prgrms.mukvengers.global.security.oauth.dto.AuthUserInfo;
import com.prgrms.mukvengers.global.security.oauth.service.OAuthService;
import com.prgrms.mukvengers.global.security.token.dto.response.TokenResponse;
import com.prgrms.mukvengers.global.security.token.service.TokenService;
import com.prgrms.mukvengers.global.utils.CookieUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuthAuthenticationSuccessHandler
	extends SavedRequestAwareAuthenticationSuccessHandler {

	private final OAuthService oauthService;
	private final TokenService tokenService;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
		Authentication authentication) throws IOException, ServletException {

		if (authentication instanceof OAuth2AuthenticationToken authenticationToken) {
			OAuth2User oauth2User = authenticationToken.getPrincipal();
			String providerName = authenticationToken.getAuthorizedClientRegistrationId();
			AuthUserInfo authUserInfo = oauthService.login(oauth2User, providerName);
			TokenResponse token = tokenService.createToken(authUserInfo);
			String redirectUrl = getRedirectUrl(request, token.accessToken());
			setResponse(response, token.refreshToken());
			getRedirectStrategy().sendRedirect(request, response, redirectUrl);

		} else {
			super.onAuthenticationSuccess(request, response, authentication);
		}
	}

	private String getRedirectUrl(HttpServletRequest request, String accessToken) {
		String targetUrl = CookieUtil.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
			.map(Cookie::getValue)
			.orElse(getDefaultTargetUrl());

		return UriComponentsBuilder.fromUriString(targetUrl)
			.queryParam("accessToken", accessToken) // url에도 실어 보내기
			.build().toUriString();
	}

	private void setResponse(HttpServletResponse response, String refreshToken) {
		response.addCookie(new Cookie("refreshToken", refreshToken)); // 임시로 쿠키에 담아서 보내기
	}
}
