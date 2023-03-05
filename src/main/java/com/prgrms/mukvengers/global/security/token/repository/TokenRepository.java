package com.prgrms.mukvengers.global.security.token.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prgrms.mukvengers.global.security.token.model.Token;

public interface TokenRepository extends JpaRepository<Token, String> {

	Optional<Token> findByUserId(Long refreshToken);
}