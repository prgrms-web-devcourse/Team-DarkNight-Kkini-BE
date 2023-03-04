package com.prgrms.mukvengers.global.security.token.dto.response;

public record TokenResponse(
	Long userId,
	String accessToken,
	String refreshToken
) {
}
