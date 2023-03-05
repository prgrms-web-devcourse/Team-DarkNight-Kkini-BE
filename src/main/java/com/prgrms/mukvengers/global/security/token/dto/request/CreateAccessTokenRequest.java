package com.prgrms.mukvengers.global.security.token.dto.request;

import javax.validation.constraints.NotBlank;

public record CreateAccessTokenRequest(
	@NotBlank String refreshToken
) {
}