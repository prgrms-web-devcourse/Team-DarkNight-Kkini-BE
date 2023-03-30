package com.prgrms.mukvengers.global.security.token.service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import javax.validation.constraints.NotBlank;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prgrms.mukvengers.global.security.oauth.dto.AuthUserInfo;
import com.prgrms.mukvengers.global.security.token.dto.Tokens;
import com.prgrms.mukvengers.global.security.token.dto.jwt.JwtAuthentication;
import com.prgrms.mukvengers.global.security.token.dto.jwt.JwtAuthenticationToken;
import com.prgrms.mukvengers.global.security.token.exception.NotFoundCookieException;
import com.prgrms.mukvengers.global.security.token.exception.RefreshTokenNotFoundException;
import com.prgrms.mukvengers.global.security.token.model.RefreshToken;
import com.prgrms.mukvengers.global.security.token.repository.RefreshTokenRepository;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TokenService { // TODO: authService와 TokenService로 분리하는 것이 좋을지 고민해보기

	private final JwtTokenProvider jwtTokenProvider;
	private final RefreshTokenRepository refreshTokenRepository;

	@Value("${jwt.expiry-seconds.refresh-token}")
	private int refreshTokenExpirySeconds;

	public Tokens createTokens(AuthUserInfo userInfo) {
		Long userId = userInfo.id();
		String userRole = userInfo.role();

		String accessToken = createAccessToken(userId, userRole);
		String refreshToken = createRefreshToken(userId, userRole);

		return new Tokens(accessToken, refreshToken);
	}

	@Transactional
	public String createRefreshToken(Long userId, String userRole) {
		RefreshToken refreshToken
			= new RefreshToken(UUID.randomUUID().toString(), userId, userRole, refreshTokenExpirySeconds);

		return refreshTokenRepository.save(refreshToken).getRefreshToken();
	}

	@Transactional
	public String getAccessTokensByRefreshToken(@NotBlank String refreshToken) {

		checkRefreshToken(refreshToken);

		return refreshTokenRepository.findById(refreshToken)
			.map(token -> createAccessToken(token.getUserId(), token.getRole()))
			.orElseThrow(RefreshTokenNotFoundException::new);
	}

	@Transactional
	public void deleteRefreshToken(String refreshToken) {

		checkRefreshToken(refreshToken);

		refreshTokenRepository.findById(refreshToken)
			.ifPresent(refreshTokenRepository::delete);
	}

	public String createAccessToken(Long userId, String userRole) {
		return jwtTokenProvider.createAccessToken(userId, userRole);
	}

	public JwtAuthenticationToken getAuthenticationByAccessToken(String accessToken) {

		jwtTokenProvider.validateToken(accessToken);

		Claims claims = jwtTokenProvider.getClaims(accessToken);

		Long userId = claims.get("userId", Long.class);
		String role = claims.get("role", String.class);

		JwtAuthentication principal = new JwtAuthentication(userId, accessToken);
		List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(role));

		return new JwtAuthenticationToken(principal, null, authorities);
	}


	public int getRefreshTokenExpirySeconds() {
		return refreshTokenExpirySeconds;
	}

	private void checkRefreshToken(String refreshToken) {
		if (Objects.isNull(refreshToken) || refreshToken.isBlank()) {
			throw new NotFoundCookieException();
		}
	}
}
