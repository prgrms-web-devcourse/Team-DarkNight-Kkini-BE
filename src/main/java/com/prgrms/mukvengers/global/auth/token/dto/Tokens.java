package com.prgrms.mukvengers.global.auth.token.dto;

public record Tokens(
	String accessToken,
	String refreshToken
) {
}
