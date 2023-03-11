package com.prgrms.mukvengers.global.utils;

import static lombok.AccessLevel.*;
import static org.springframework.http.HttpHeaders.*;

import javax.servlet.http.HttpServletRequest;

import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public class ExtractUtil {

	private static final String BEARER_TYPE = "Bearer";

	public static String extractTokenFromRequest(HttpServletRequest request) {
		String authHeaderValue = request.getHeader(AUTHORIZATION);
		if (authHeaderValue != null) {
			return extractToken(authHeaderValue);
		}
		return null;
	}

	public static String extractToken(String authHeaderValue) {
		if (authHeaderValue.toLowerCase().startsWith(BEARER_TYPE.toLowerCase())) {
			return authHeaderValue.substring(BEARER_TYPE.length()).trim();
		}
		return null;
	}
}
