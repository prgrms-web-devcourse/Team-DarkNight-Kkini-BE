package com.prgrms.mukvengers.global.security.token.model;

import static lombok.AccessLevel.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Token {

	@Id
	@Column(nullable = false, unique = true)
	private String refreshToken;

	@Column(nullable = false, unique = true)
	private long userId;

	public Token(String refreshToken, Long userId) {
		this.refreshToken = refreshToken;
		this.userId = userId;
	}

}
