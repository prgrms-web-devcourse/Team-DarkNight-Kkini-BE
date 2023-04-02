package com.prgrms.mukvengers.global.security.token.dto;

public record Tokens(
	String accessToken,
	String refreshToken
) {
}
