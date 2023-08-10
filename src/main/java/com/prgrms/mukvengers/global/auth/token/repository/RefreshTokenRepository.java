package com.prgrms.mukvengers.global.auth.token.repository;

import org.springframework.data.repository.CrudRepository;

import com.prgrms.mukvengers.global.auth.token.model.RefreshToken;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {
}
