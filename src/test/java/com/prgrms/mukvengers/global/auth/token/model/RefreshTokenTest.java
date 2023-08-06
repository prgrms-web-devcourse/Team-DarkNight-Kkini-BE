package com.prgrms.mukvengers.global.auth.token.model;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.atomic.AtomicReference;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

class RefreshTokenTest {

	private String validRefreshToken;
	private Long validUserId;
	private String validRole;
	private long validExpiration;

	@BeforeEach
	void setUp() {
		validRefreshToken = "validRefreshToken";
		validUserId = 123L;
		validRole = "ROLE_USER";
		validExpiration = 3600L;
	}

	@Test
	@DisplayName("[성공] RefreshToken 객체 생성")
	void createRefreshToken_success() {
		final AtomicReference<RefreshToken> refreshToken = new AtomicReference<>();

		assertDoesNotThrow(
			() -> refreshToken.set(RefreshToken.builder()
				.refreshToken(validRefreshToken)
				.userId(validUserId)
				.role(validRole)
				.expiration(validExpiration)
				.build())
		);

		assertThat(refreshToken.get().getRefreshToken()).isEqualTo(validRefreshToken);
		assertThat(refreshToken.get().getUserId()).isEqualTo(validUserId);
		assertThat(refreshToken.get().getRole()).isEqualTo(validRole);
		assertThat(refreshToken.get().getExpiration()).isEqualTo(validExpiration);
	}

	@ParameterizedTest
	@NullAndEmptySource
	@DisplayName("[실패] RefreshToken 값이 빈값 또는 null이면 생성할 수 없다.")
	void createRefreshToken_WithInvalidRefreshToken_ShouldThrowException(String invalidRefreshToken) {
		assertThatThrownBy(() -> RefreshToken.builder()
			.refreshToken(invalidRefreshToken)
			.userId(validUserId)
			.role(validRole)
			.expiration(validExpiration)
			.build())
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("올바르지 않은 토큰 값");
	}

	@ParameterizedTest
	@NullSource
	@ValueSource(longs = {0L, -1L})
	@DisplayName("[실패] 유저 아이디는 null 혹은 0보다 작을 수 없다.")
	void createRefreshToken_WithInvalidUserId_ShouldThrowException(Long invalidUserId) {
		assertThatThrownBy(() -> RefreshToken.builder()
			.refreshToken(validRefreshToken)
			.userId(invalidUserId)
			.role(validRole)
			.expiration(validExpiration)
			.build())
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("올바르지 않은 유저 아이디");
	}

	@ParameterizedTest
	@NullAndEmptySource
	@DisplayName("[실패] 역할은 빈값 또는 null로 생성할 수 없다.")
	void createRefreshToken_WithInvalidRole_ShouldThrowException(String invalidRole) {
		assertThatThrownBy(() -> RefreshToken.builder()
			.refreshToken(validRefreshToken)
			.userId(validUserId)
			.role(invalidRole)
			.expiration(validExpiration)
			.build())
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("올바르지 않은 권한");
	}

	@Test
	@DisplayName("[실패] 시간을 0이하로 설정해서 생성할 수 없다.")
	void createRefreshToken_WithInvalidExpiration_ShouldThrowException() {
		assertThatThrownBy(() -> RefreshToken.builder()
			.refreshToken(validRefreshToken)
			.userId(validUserId)
			.role(validRole)
			.expiration(-1L)
			.build())
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("올바르지 않은 만료 시간");
	}
}
