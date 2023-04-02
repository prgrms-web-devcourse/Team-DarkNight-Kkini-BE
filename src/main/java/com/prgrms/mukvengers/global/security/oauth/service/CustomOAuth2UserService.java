package com.prgrms.mukvengers.global.security.oauth.service;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.prgrms.mukvengers.domain.user.service.UserService;
import com.prgrms.mukvengers.global.security.oauth.dto.AuthUserInfo;
import com.prgrms.mukvengers.global.security.oauth.dto.CustomOAuth2User;
import com.prgrms.mukvengers.global.security.oauth.dto.OAuthUserInfo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

	private final UserService userService;

	@Override
	public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
		OAuth2User oauth2User = super.loadUser(oAuth2UserRequest);
		String providerName = oAuth2UserRequest.getClientRegistration().getRegistrationId();

		OAuthUserInfo oauthUserInfo = extractUserInfoFromOAuth2User(oauth2User, providerName);

		AuthUserInfo user = userService.getOrRegisterUser(oauthUserInfo);

		return new CustomOAuth2User(user, oauth2User.getAttributes());
	}

	private OAuthUserInfo extractUserInfoFromOAuth2User(OAuth2User oauth2User, String providerName) {
		return OAuthProvider
			.getOAuthProviderByName(providerName)
			.toUserInfo(oauth2User);
	}
}
