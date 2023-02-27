package com.prgrms.mukvengers.global.security.oauth.service;

import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prgrms.mukvengers.domain.user.model.User;
import com.prgrms.mukvengers.domain.user.repository.UserRepository;
import com.prgrms.mukvengers.global.security.jwt.JwtTokenProvider;
import com.prgrms.mukvengers.global.security.oauth.dto.OAuthUserInfo;
import com.prgrms.mukvengers.global.security.oauth.mapper.OAuthMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class OAuthService {

	private final JwtTokenProvider jwtTokenProvider;
	private final UserRepository userRepository;
	private final OAuthMapper oauthMapper;

	@Transactional
	public String login(OAuth2User oauth2User, String providerName) {

		OAuthUserInfo oauthUserInfo = OAuthProvider
			.getOAuthProviderByName(providerName)
			.toUserInfo(oauth2User);

		// TODO: user로 옮겨야 하나?
		User user = userRepository
			.findByUserIdByProviderAndOauthId(oauthUserInfo.provider(), oauthUserInfo.oauthId())
			.orElseGet(() -> userRepository.save(oauthMapper.toUser(oauthUserInfo)));

		String accessToken = jwtTokenProvider.createAccessToken(user.getId(), "ROLE_USER");
		log.debug("{}를 통해 로그인 한 사용자(ID: {}), accessToken = {}", providerName, user.getId(), accessToken);
		return accessToken;
	}
}