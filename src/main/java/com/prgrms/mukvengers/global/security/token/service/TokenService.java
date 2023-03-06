package com.prgrms.mukvengers.global.security.token.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prgrms.mukvengers.global.security.jwt.JwtTokenProvider;
import com.prgrms.mukvengers.global.security.oauth.dto.AuthUserInfo;
import com.prgrms.mukvengers.global.security.token.dto.request.RefreshTokenRequest;
import com.prgrms.mukvengers.global.security.token.dto.response.TokenResponse;
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

	@Transactional
	public TokenResponse createToken(AuthUserInfo user) {

		String accessToken = jwtTokenProvider.createAccessToken(user.id(), user.role());
		RefreshToken refreshToken
			= new RefreshToken(createRefreshToken(), user.id(), refreshTokenExpirySeconds);

		refreshTokenRepository.save(refreshToken);

		return new TokenResponse(user.id(), accessToken, refreshToken.getRefreshToken());
	}

	@Transactional
	public TokenResponse renewTokens(RefreshTokenRequest refreshTokenRequest) {
		String refreshToken = refreshTokenRequest.refreshToken();

		RefreshToken token = refreshTokenRepository.findById(refreshToken)
			.orElseThrow(() -> new RefreshTokenNotFoundException(refreshToken));

		refreshTokenRepository.delete(token);
		AuthUserInfo user = new AuthUserInfo(token.getUserId(), "USER");

		return createToken(user);
	}

	@Transactional
	public void deleteRefreshToken(RefreshTokenRequest refreshTokenRequest) {
		String refreshToken = refreshTokenRequest.refreshToken();

		refreshTokenRepository.findById(refreshToken)
			.ifPresentOrElse(refreshTokenRepository::delete,
				() -> {
					throw new RefreshTokenNotFoundException(refreshToken);
				});
	}

	public String createRefreshToken() {
		return UUID.randomUUID().toString();
	}

}
