package com.prgrms.mukvengers.global.security.token.api;

import static org.springframework.http.HttpStatus.*;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.prgrms.mukvengers.global.common.dto.ApiResponse;
import com.prgrms.mukvengers.global.security.jwt.JwtAuthentication;
import com.prgrms.mukvengers.global.security.token.dto.request.CreateAccessTokenRequest;
import com.prgrms.mukvengers.global.security.token.dto.response.TokenResponse;
import com.prgrms.mukvengers.global.security.token.service.TokenService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/tokens")
public class TokenController {

	private final TokenService tokenService;

	@PostMapping
	public ResponseEntity<ApiResponse<TokenResponse>> getTokensByRefreshToken(
		@RequestBody @Valid CreateAccessTokenRequest createAccessTokenRequest
	) {
		TokenResponse response = tokenService.renewTokens(createAccessTokenRequest);

		return ResponseEntity.ok().body(new ApiResponse<>(response));
	}

	@DeleteMapping
	@ResponseStatus(NO_CONTENT)
	public void deleteRefreshToken(
		@AuthenticationPrincipal JwtAuthentication user
	) {
		tokenService.deleteTokenByUserId(user.id());
	}
}
