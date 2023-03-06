package com.prgrms.mukvengers.global.security.token.model;

import static lombok.AccessLevel.*;

import java.util.Objects;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = PROTECTED)
@RedisHash(value = "refreshToken")
public class RefreshToken {

	@Id
	private String refreshToken;

	private Long userId;

	@TimeToLive
	private long expiration;

	@Builder
	public RefreshToken(String refreshToken, Long userId, long expiration) {
		this.refreshToken = checkRefreshToken(refreshToken);
		this.userId = checkUserId(userId);
		this.expiration = checkExpiration(expiration);
	}

	private String checkRefreshToken(String refreshToken) {
		if (!Objects.nonNull(refreshToken) || refreshToken.isBlank()) {
			throw new IllegalArgumentException("올바르지 않은 토큰 값");
		}
		return refreshToken;
	}

	private Long checkUserId(Long userId) {
		if (!Objects.nonNull(userId) || userId < 1) {
			throw new IllegalArgumentException("올바르지 않은 유저 아이디");
		}
		return userId;
	}

	private long checkExpiration(long expiration) {
		if (expiration < 1) {
			throw new IllegalArgumentException("올바르지 않은 만료 시간");
		}
		return expiration;
	}
}