package com.prgrms.mukvengers.global.security.oauth.dto;

import lombok.Builder;

@Builder
public record OAuthUserInfo(
	String nickname,
	String profileImgUrl,
	String provider,
	String oauthId
) {
}
