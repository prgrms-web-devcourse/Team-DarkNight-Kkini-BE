package com.prgrms.mukvengers.global.security.token.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.prgrms.mukvengers.base.SliceTest;
import com.prgrms.mukvengers.global.security.oauth.dto.AuthUserInfo;
import com.prgrms.mukvengers.global.security.token.dto.Tokens;
import com.prgrms.mukvengers.global.security.token.dto.jwt.JwtAuthentication;
import com.prgrms.mukvengers.global.security.token.dto.jwt.JwtAuthenticationToken;
import com.prgrms.mukvengers.global.security.token.exception.InvalidTokenException;
import com.prgrms.mukvengers.global.security.token.exception.NotFoundCookieException;
import com.prgrms.mukvengers.global.security.token.exception.RefreshTokenNotFoundException;
import com.prgrms.mukvengers.global.security.token.model.RefreshToken;
import com.prgrms.mukvengers.global.security.token.repository.RefreshTokenRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.impl.DefaultClaims;

@SpringBootTest(classes = {TokenService.class})
class TokenServiceTest extends SliceTest {

	@MockBean
	private JwtTokenProvider jwtTokenProvider;

	@MockBean
	private RefreshTokenRepository refreshTokenRepository;

	@Autowired
	private TokenService tokenService;

	private AuthUserInfo userInfo;
	private String accessToken;
	private String refreshToken;

	@BeforeEach
	void setUp() {
		userInfo = new AuthUserInfo(1L, "user", "ROLE_USER");
		accessToken = "testAccessToken";
		refreshToken = "testRefreshToken";
	}

	@Test
	@DisplayName("[성공] 인증된 유저 정보(로그인 성공시)를 통해 accessToken, refreshToken를 발급받을 수 있다.")
	void createTokens_success() {
		// Given
		given(jwtTokenProvider.createAccessToken(any(Long.class), any(String.class))).willReturn(accessToken);
		given(refreshTokenRepository.save(any(RefreshToken.class))).willReturn(getRefreshToken());

		// When
		Tokens tokens = tokenService.createTokens(userInfo);

		// Then
		then(jwtTokenProvider).should().createAccessToken(any(Long.class), any(String.class));
		then(refreshTokenRepository).should().save(any(RefreshToken.class));
		assertThat(tokens.accessToken()).isEqualTo(accessToken);
		assertThat(tokens.refreshToken()).isEqualTo(refreshToken);
	}

	@Test
	@DisplayName("[성공] refresh token을 통해 accessToken을 재발급 받을 수 있다.")
	void getAccessTokensByRefreshToken_success() {
		// Given
		given(jwtTokenProvider.createAccessToken(any(Long.class), any(String.class))).willReturn(
			accessToken); // 새로운 access token 재발급
		given(refreshTokenRepository.findById(any(String.class))).willReturn(Optional.of(getRefreshToken()));

		// When
		String newAccessToken = tokenService.getAccessTokensByRefreshToken(refreshToken);

		// Then
		then(jwtTokenProvider).should().createAccessToken(any(Long.class), any(String.class));
		then(refreshTokenRepository).should().findById(any(String.class));
		assertThat(newAccessToken).isEqualTo(accessToken);
	}

	@Test
	@DisplayName("[성공] 저장소에 저장된 refreshToken을 삭제할 수 있다.")
	void deleteRefreshToken_success() {
		// Given
		given(refreshTokenRepository.findById(any(String.class))).willReturn(Optional.of(getRefreshToken()));

		// When
		tokenService.deleteRefreshToken(refreshToken);

		// Then
		then(refreshTokenRepository).should().findById(any(String.class));
		then(refreshTokenRepository).should().delete(any(RefreshToken.class));
	}

	@ParameterizedTest
	@NullAndEmptySource
	@DisplayName("[실패] refreshToken이 올바르지 않을 경우(null, empty)에 예외가 발생한다.")
	void getAccessTokensByRefreshToken_fail_invalid(String invalidRefreshToken) {
		// When & Then
		assertAll(
			() -> assertThrows(NotFoundCookieException.class,
				() -> tokenService.getAccessTokensByRefreshToken(invalidRefreshToken)),
			() -> assertThrows(NotFoundCookieException.class,
				() -> tokenService.deleteRefreshToken(invalidRefreshToken))
		);
	}

	@Test
	@DisplayName("[실패] accessToken을 재발급 받으려는 경우에 refreshToken이 저장소에 존재하지 않을 경우에 예외가 발생한다.")
	void getAccessTokensByRefreshToken_fail() {
		// Given
		given(refreshTokenRepository.findById(any(String.class))).willReturn(Optional.empty());

		// When & Then
		assertThrows(RefreshTokenNotFoundException.class,
			() -> tokenService.getAccessTokensByRefreshToken(refreshToken));

		then(refreshTokenRepository).should().findById(any(String.class));
	}

	@Test
	@DisplayName("[실패] refreshToken을 삭제하는 경우에 만약 저장소에 존재하지 않을 경우에도 예외는 발생하지 않는다.")
	void deleteRefreshToken_fail() {
		// Given
		given(refreshTokenRepository.findById(any(String.class))).willReturn(Optional.empty());

		// When & Then
		assertDoesNotThrow(() -> tokenService.deleteRefreshToken(refreshToken));
		then(refreshTokenRepository).should().findById(any(String.class));
		then(refreshTokenRepository).should(never()).delete(any(RefreshToken.class));
	}

	@Test
	@DisplayName("[성공] 유저 정보를 통해서 accessToken을 발급할 수 있다.")
	void createAccessToken_success() {
		// Given
		when(jwtTokenProvider.createAccessToken(any(Long.class), any(String.class))).thenReturn(accessToken);

		// When
		String providedAccessToken = tokenService.createAccessToken(userInfo.id(), userInfo.role());

		// Then
		then(jwtTokenProvider).should().createAccessToken(any(Long.class), any(String.class));
		assertThat(providedAccessToken).isEqualTo(accessToken);
	}

	@Test
	@DisplayName("[성공] accessToken을 통해서 인증된 유저 정보를 가져올 수 있다.")
	void getAuthenticationByAccessToken_success() {
		// Given
		Claims claims = new DefaultClaims(Map.of("userId", userInfo.id(), "role", userInfo.role()));
		willDoNothing().given(jwtTokenProvider).validateToken(any(String.class));
		given(jwtTokenProvider.getClaims(any(String.class))).willReturn(claims);

		// When
		JwtAuthenticationToken jwtAuthenticationToken = tokenService.getAuthenticationByAccessToken(accessToken);

		JwtAuthentication principal = new JwtAuthentication(userInfo.id(), accessToken);
		List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(userInfo.role()));

		// Then
		assertThat(jwtAuthenticationToken).isNotNull();
		assertThat(jwtAuthenticationToken.getPrincipal()).isEqualTo(principal);
		assertThat(jwtAuthenticationToken.getAuthorities()).isEqualTo(authorities);
		then(jwtTokenProvider).should().validateToken(any(String.class));
		then(jwtTokenProvider).should().getClaims(any(String.class));
	}

	@Test
	@DisplayName("[성공] accessToken을 통해서 인증된 유저 정보를 가져올 수 있다.")
	void getAuthenticationByAccessToken_fail() {
		// Given
		willThrow(new InvalidTokenException()).given(jwtTokenProvider).validateToken(any(String.class));

		// When & Then
		assertThrows(InvalidTokenException.class, () -> tokenService.getAuthenticationByAccessToken(accessToken));
	}

	private RefreshToken getRefreshToken() {
		return new RefreshToken(refreshToken, 1L, "ROLE_USER", 3600);
	}
}