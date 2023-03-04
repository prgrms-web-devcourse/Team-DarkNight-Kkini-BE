package com.prgrms.mukvengers.domain.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.prgrms.mukvengers.domain.user.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

	@Query("""
		SELECT u
		FROM User u
		WHERE u.provider = :provider
		AND u.oauthId = :oauthId
		""")
	Optional<User> findByUserIdByProviderAndOauthId(
		@Param("provider") String provider,
		@Param("oauthId") String oauthId);
}
