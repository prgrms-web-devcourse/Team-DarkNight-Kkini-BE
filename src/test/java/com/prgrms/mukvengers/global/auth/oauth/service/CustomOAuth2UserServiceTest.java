package com.prgrms.mukvengers.global.auth.oauth.service;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.*;
import static org.springframework.security.oauth2.core.OAuth2AccessToken.*;

import java.time.Instant;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.prgrms.mukvengers.domain.user.service.UserService;
import com.prgrms.mukvengers.global.auth.oauth.dto.AuthUserInfo;
import com.prgrms.mukvengers.global.auth.oauth.dto.CustomOAuth2User;
import com.prgrms.mukvengers.global.auth.oauth.dto.OAuthUserInfo;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureWireMock(port = 8888)
class CustomOAuth2UserServiceTest {

	private final String providerUserInfoEndpoint = "/api/user-info";

	private final OAuth2UserRequest oauth2UserRequest
		= new OAuth2UserRequest(getClientRegistration("kakao"), createAccessToken());

	@MockBean
	private UserService userService;

	@Autowired
	private OAuth2UserService<OAuth2UserRequest, OAuth2User> oauth2UserService;

	@Test
	@DisplayName("외부 (kakao, google) API를 이용해서 소셜 로그인을 할 수 있다.")
	void loadUserTest_success() { // 카카오 테스트

		// Given
		AuthUserInfo userInfo = new AuthUserInfo(1L, "test", "ROLE_USER");
		given(userService.getOrRegisterUser(any(OAuthUserInfo.class))).willReturn(userInfo);

		String mockResponse = "{\"id\": 1, \"properties\": {\"nickname\": \"test\", \"profile_image\": \"image.jpg\"}}";

		// 설정된 외부 API 응답을 모의합니다.
		stubFor(WireMock.get(urlEqualTo(providerUserInfoEndpoint))
			.willReturn(WireMock.aResponse()
				.withHeader("Content-Type", "application/json")
				.withStatus(200)
				.withBody(mockResponse)));

		OAuth2UserRequest oauth2UserRequest
			= new OAuth2UserRequest(getClientRegistration("kakao"), createAccessToken());

		// When
		OAuth2User oauth2User = oauth2UserService.loadUser(oauth2UserRequest);
		Map<String, Object> attributes = oauth2User.getAttributes();
		Map<String, Object> properties = oauth2User.getAttribute("properties");

		// Assert
		assertThat(oauth2User).isInstanceOf(CustomOAuth2User.class);
		assertThat(oauth2User.getAuthorities()).isNotEmpty();
		assertThat(attributes).containsEntry("id", 1);
		assertThat(properties).containsEntry("nickname", "test")
			.containsEntry("profile_image", "image.jpg");
	}

	@Test
	@DisplayName("[실패] 로그인에 실패하거나 응답이 올바르지 않은 경우 예외가 발생한다.")
	void testLoadUser_whenExternalApiFails() {

		// given
		stubFor(WireMock.get(urlEqualTo(providerUserInfoEndpoint))
			.willReturn(WireMock.aResponse()
				.withStatus(500)));

		// when & then
		assertThatThrownBy(() -> oauth2UserService.loadUser(oauth2UserRequest))
			.isInstanceOf(OAuth2AuthenticationException.class);
	}

	private ClientRegistration getClientRegistration(String registrationId) {
		return ClientRegistration.withRegistrationId(registrationId)
			.clientId("clientId")
			.clientSecret("clientSecret")
			.authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
			.redirectUri("{baseUrl}/{action}/oauth2/code/{registrationId}")
			.scope("read")
			.authorizationUri("http://localhost:" + 8888 + "/oauth2/authorize")
			.tokenUri("http://localhost:" + 8888 + "/oauth2/token")
			.userInfoUri("http://localhost:" + 8888 + providerUserInfoEndpoint)
			.userNameAttributeName("id")
			.clientName("Test Client")
			.build();
	}

	private OAuth2AccessToken createAccessToken() {
		return new OAuth2AccessToken(
			TokenType.BEARER, "testAccessToken", Instant.now(), Instant.now().plusSeconds(3600));
	}

}
