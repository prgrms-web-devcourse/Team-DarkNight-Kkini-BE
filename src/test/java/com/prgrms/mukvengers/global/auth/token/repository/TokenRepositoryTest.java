package com.prgrms.mukvengers.global.auth.token.repository;

import static org.assertj.core.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest;
import org.springframework.context.annotation.Import;

import com.prgrms.mukvengers.global.auth.token.model.RefreshToken;
import com.prgrms.mukvengers.global.config.EmbeddedRedisConfig;

@DataRedisTest
@Import(EmbeddedRedisConfig.class)
class TokenRepositoryTest {

	private final String key = "refreshToken";
	private final Long value = 1L;
	private final String value2 = "role";
	private final Long expiration = 10000L;

	@Autowired
	private RefreshTokenRepository tokenRepository;

	@BeforeEach
	void setUp() {
		RefreshToken token = createRefreshToken();
		tokenRepository.save(token);
	}

	@Test
	@DisplayName("[성공] 레디스에 refresh token을 저장할 수 있다.")
	void refreshTokenSaveTestWithRedis_success() {

		// When
		Optional<RefreshToken> foundToken = tokenRepository.findById(key);

		// Then
		assertThat(foundToken).isPresent();
		assertThat(foundToken.get().getRefreshToken()).isEqualTo(key);
		assertThat(foundToken.get().getUserId()).isEqualTo(value);
		assertThat(foundToken.get().getRole()).isEqualTo(value2);
		assertThat(foundToken.get().getExpiration()).isEqualTo(expiration);
	}

	private RefreshToken createRefreshToken() {
		return RefreshToken.builder()
			.refreshToken(key)
			.userId(value)
			.role(value2)
			.expiration(expiration)
			.build();
	}
}
