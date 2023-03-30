package com.prgrms.mukvengers.global.security.oauth.handler;

import static com.prgrms.mukvengers.global.security.oauth.repository.HttpCookieOAuthAuthorizationRequestRepository.*;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import com.prgrms.mukvengers.global.security.oauth.dto.CustomOAuth2User;
import com.prgrms.mukvengers.global.security.token.dto.Tokens;
import com.prgrms.mukvengers.global.security.token.service.TokenService;
import com.prgrms.mukvengers.global.utils.CookieUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuthAuthenticationSuccessHandler
	extends SavedRequestAwareAuthenticationSuccessHandler {

	private final TokenService tokenService;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
		Authentication authentication) throws IOException, ServletException {

		if (authentication instanceof CustomOAuth2User oauth2User) {

			Tokens tokens = tokenService.createTokens(oauth2User.getUserInfo());

			String targetUrl = determineTargetUrl(request, tokens.accessToken());
			setRefreshTokenInCookie(response, tokens.refreshToken());
			getRedirectStrategy().sendRedirect(request, response, targetUrl);

		} else {
			super.onAuthenticationSuccess(request, response, authentication);
		}
	}

	private String determineTargetUrl(HttpServletRequest request, String accessToken) {
		String targetUrl = CookieUtil.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
			.map(Cookie::getValue)
			.orElse(getDefaultTargetUrl());

		return UriComponentsBuilder.fromUriString(targetUrl)
			.queryParam("accessToken", accessToken)
			.build().toUriString();
	}

	private void setRefreshTokenInCookie(HttpServletResponse response, String refreshToken) {
		ResponseCookie token = ResponseCookie.from("refreshToken", refreshToken)
			.path(getDefaultTargetUrl())
			.httpOnly(true)
			.sameSite("None")
			.secure(true)
			.maxAge(tokenService.getRefreshTokenExpirySeconds())
			.build();

		response.addHeader("Set-Cookie", token.toString());
	}
}
