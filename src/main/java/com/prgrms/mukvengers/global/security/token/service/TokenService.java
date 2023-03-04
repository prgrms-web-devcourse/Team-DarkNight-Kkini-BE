package com.prgrms.mukvengers.global.security.token.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prgrms.mukvengers.domain.user.exception.UserNotFoundException;
import com.prgrms.mukvengers.global.security.jwt.JwtTokenProvider;
import com.prgrms.mukvengers.global.security.oauth.dto.AuthUserInfo;
import com.prgrms.mukvengers.global.security.token.dto.request.CreateAccessTokenRequest;
import com.prgrms.mukvengers.global.security.token.dto.response.TokenResponse;
import com.prgrms.mukvengers.global.security.token.exception.RefreshTokenNotFoundException;
import com.prgrms.mukvengers.global.security.token.model.Token;
import com.prgrms.mukvengers.global.security.token.repository.TokenRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TokenService {

	private final JwtTokenProvider jwtTokenProvider;
	private final TokenRepository tokenRepository;

	@Transactional
	public TokenResponse createToken(AuthUserInfo user) {

		String accessToken = jwtTokenProvider.createAccessToken(user.id(), user.role());
		String refreshToken = jwtTokenProvider.createRefreshToken();

		tokenRepository.save(new Token(refreshToken, user.id()));

		return new TokenResponse(user.id(), accessToken, refreshToken);
	}

	@Transactional
	public TokenResponse renewTokens(CreateAccessTokenRequest createAccessTokenRequest) {
		String refreshToken = createAccessTokenRequest.refreshToken();

		Token token = tokenRepository.findById(refreshToken)
			.orElseThrow(() -> new RefreshTokenNotFoundException(refreshToken));

		tokenRepository.delete(token);
		AuthUserInfo user = new AuthUserInfo(token.getUserId(), "USER");

		return createToken(user);
	}

	@Transactional
	public void deleteTokenByUserId(Long userId) {
		tokenRepository.findByUserId(userId)
			.ifPresentOrElse(tokenRepository::delete,
				() -> {
					throw new UserNotFoundException(userId);
				});
	}
}
