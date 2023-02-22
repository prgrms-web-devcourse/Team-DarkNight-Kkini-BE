package com.prgrms.mukvengers.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prgrms.mukvengers.domain.user.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

}
