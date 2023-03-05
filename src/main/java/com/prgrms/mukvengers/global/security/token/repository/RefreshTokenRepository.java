package com.prgrms.mukvengers.global.security.token.repository;

import org.springframework.data.repository.CrudRepository;

import com.prgrms.mukvengers.global.security.token.model.RefreshToken;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {
}
