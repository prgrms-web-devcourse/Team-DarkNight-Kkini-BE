package com.prgrms.mukvengers.global.security.token.api;

import static org.springframework.http.HttpHeaders.*;

import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prgrms.mukvengers.global.security.token.dto.response.TokenResponse;
import com.prgrms.mukvengers.global.security.token.service.TokenService;
import com.prgrms.mukvengers.global.utils.CookieUtil;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/tokens")
public class TokenController {

	private final TokenService tokenService;

	@PostMapping
	public ResponseEntity<TokenResponse> refreshAccessToken(
		@CookieValue("refreshToken") String refreshToken
	) {
		String accessToken = tokenService.getAccessTokensByRefreshToken(refreshToken);

		return ResponseEntity.ok()
			.body(new TokenResponse(accessToken));
	}

	@DeleteMapping
	public ResponseEntity<Void> expireRefreshToken(
		@CookieValue("refreshToken") String refreshToken
	) {
		tokenService.deleteRefreshToken(refreshToken);

		ResponseCookie emptyCookie = CookieUtil.getEmptyCookie("refreshToken");

		return ResponseEntity.noContent()
			.header(SET_COOKIE, emptyCookie.toString()).build();
	}
}
