package com.prgrms.mukvengers.global.auth.oauth.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.prgrms.mukvengers.global.auth.oauth.dto.OAuthUserInfo;

class OAuthProviderTest {

	@Test
	void kakaoToUserInfoTest_success() {
		// Given
		Map<String, Object> properties = new HashMap<>();
		properties.put("nickname", "kakao_nickname");
		properties.put("profile_image", "kakao_profile_image");

		Map<String, Object> attributes = new HashMap<>();
		attributes.put("id", 123L);
		attributes.put("properties", properties);

		OAuth2User oAuth2User = new DefaultOAuth2User(Set.of(), attributes, "id");

		// When
		OAuthUserInfo userInfo = OAuthProvider.KAKAO.toUserInfo(oAuth2User);

		// Then
		assertThat(userInfo.provider()).isEqualTo("kakao");
		assertThat(userInfo.oauthId()).isEqualTo("123");
		assertThat(userInfo.nickname()).isEqualTo("kakao_nickname");
		assertThat(userInfo.profileImgUrl()).isEqualTo("kakao_profile_image");
	}

	@Test
	void googleToUserInfoTest_success() {
		// Given
		Map<String, Object> attributes = new HashMap<>();
		attributes.put("sub", "google_id");
		attributes.put("name", "google_name");
		attributes.put("picture", "google_picture");

		OAuth2User oAuth2User = new DefaultOAuth2User(Set.of(), attributes, "sub");

		// When
		OAuthUserInfo userInfo = OAuthProvider.GOOGLE.toUserInfo(oAuth2User);

		// Then
		assertThat(userInfo.provider()).isEqualTo("google");
		assertThat(userInfo.oauthId()).isEqualTo("google_id");
		assertThat(userInfo.nickname()).isEqualTo("google_name");
		assertThat(userInfo.profileImgUrl()).isEqualTo("google_picture");
	}

	@Test
	public void getOAuthProviderByNameTest_success() {

		OAuthProvider kakaoProvider = OAuthProvider.getOAuthProviderByName("kakao");
		OAuthProvider googleProvider = OAuthProvider.getOAuthProviderByName("google");

		assertThat(kakaoProvider).isEqualTo(OAuthProvider.KAKAO);
		assertThat(googleProvider).isEqualTo(OAuthProvider.GOOGLE);
	}

	@Test
	public void getOAuthProviderByName_fail_invalid() {
		// Given
		String invalidProviderName = "invalid_provider";

		// When & Then
		assertThrows(IllegalArgumentException.class,
			() -> OAuthProvider.getOAuthProviderByName(invalidProviderName));

	}
}

