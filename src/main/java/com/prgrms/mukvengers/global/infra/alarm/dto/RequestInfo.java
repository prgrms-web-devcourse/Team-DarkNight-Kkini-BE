package com.prgrms.mukvengers.global.infra.alarm.dto;

import javax.servlet.http.HttpServletRequest;

public record RequestInfo(
	String requestURL,
	String method,
	String remoteAddress
) {
	public RequestInfo(HttpServletRequest request) {
		this(String.valueOf(request.getRequestURL()),
			request.getMethod(),
			request.getRemoteAddr()
		);
	}
}
