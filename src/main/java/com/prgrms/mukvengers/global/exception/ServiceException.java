package com.prgrms.mukvengers.global.exception;

import lombok.Getter;

@Getter
public abstract class ServiceException extends RuntimeException {

	protected static final String DOT = ".";

	private final String messageKey;

	private final Object[] params;

	protected ServiceException(String messageKey, Object[] params) {
		this.messageKey = messageKey;
		this.params = params;
	}
}

