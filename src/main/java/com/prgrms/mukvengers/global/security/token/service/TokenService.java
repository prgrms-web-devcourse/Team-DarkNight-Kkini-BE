package com.prgrms.mukvengers.global.security.token.service;

import java.util.Objects;
import java.util.UUID;

import javax.validation.constraints.NotBlank;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prgrms.mukvengers.global.security.jwt.JwtTokenProvider;
import com.prgrms.mukvengers.global.security.oauth.dto.AuthUserInfo;
import com.prgrms.mukvengers.global.security.token.exception.NotFoundCookieException;
import com.prgrms.mukvengers.global.security.token.exception.RefreshTokenNotFoundException;
import com.prgrms.mukvengers.global.security.token.model.RefreshToken;
import com.prgrms.mukvengers.global.security.token.repository.RefreshTokenRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TokenService {

	private final JwtTokenProvider jwtTokenProvider;
	private final RefreshTokenRepository refreshTokenRepository;

	@Value("${jwt.expiry-seconds.refresh-token}")
	private int refreshTokenExpirySeconds;

	public String createAccessToken(AuthUserInfo user) {
		return jwtTokenProvider.createAccessToken(user.id(), user.role());
	}

	@Transactional
	public String createRefreshToken(AuthUserInfo user) {
		RefreshToken refreshToken
			= new RefreshToken(UUID.randomUUID().toString(), user.id(), user.role(), refreshTokenExpirySeconds);

		return refreshTokenRepository.save(refreshToken).getRefreshToken();
	}

	@Transactional
	public String getAccessTokensByRefreshToken(@NotBlank String refreshToken) {

		checkRefreshToken(refreshToken);

		return refreshTokenRepository.findById(refreshToken)
			.map(token -> jwtTokenProvider.createAccessToken(token.getUserId(), token.getRole()))
			.orElseThrow(RefreshTokenNotFoundException::new);
	}

	@Transactional
	public void deleteRefreshToken(String refreshToken) {

		checkRefreshToken(refreshToken);

		refreshTokenRepository.findById(refreshToken)
			.ifPresentOrElse(refreshTokenRepository::delete, RefreshTokenNotFoundException::new);
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
