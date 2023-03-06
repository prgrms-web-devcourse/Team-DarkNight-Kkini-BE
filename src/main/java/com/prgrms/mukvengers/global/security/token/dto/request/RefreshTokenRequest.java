package com.prgrms.mukvengers.global.security.token.dto.request;

import javax.validation.constraints.NotBlank;

public record RefreshTokenRequest(
	@NotBlank String refreshToken
) {
}