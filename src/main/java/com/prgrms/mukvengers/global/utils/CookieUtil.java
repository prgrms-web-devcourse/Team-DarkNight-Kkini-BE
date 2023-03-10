package com.prgrms.mukvengers.global.utils;

import static lombok.AccessLevel.*;

import java.util.Arrays;
import java.util.Base64;
import java.util.Optional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.ResponseCookie;
import org.springframework.util.SerializationUtils;

import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public class CookieUtil {

	public static Optional<Cookie> getCookie(HttpServletRequest request, String name) {

		Cookie[] cookies = request.getCookies();

		if (cookies != null && cookies.length > 0) {
			return Arrays.stream(cookies)
				.filter(cookie -> cookie.getName().equals(name))
				.findFirst();
		}

		return Optional.empty();
	}

	public static void addCookie(HttpServletResponse response, String name, String value, int maxAge) {
		Cookie cookie = new Cookie(name, value);
		cookie.setPath("/");
		cookie.setHttpOnly(false);
		cookie.setSecure(true);
		cookie.setMaxAge(maxAge);
		response.addCookie(cookie);
	}

	public static void deleteCookie(HttpServletRequest request, HttpServletResponse response, String name) {

		Cookie[] cookies = request.getCookies();

		if (cookies != null) {
			Arrays.stream(cookies)
				.filter(cookie -> cookie.getName().equals(name))
				.findFirst()
				.ifPresent(cookie -> {
					cookie.setValue("");
					cookie.setPath("/");
					cookie.setMaxAge(0);
					response.addCookie(cookie);
				});
		}
	}

	public static ResponseCookie getEmptyCookie(String key) {
		return ResponseCookie.from(key, "")
			.path("/")
			.sameSite("None")
			.httpOnly(false)
			.secure(true)
			.maxAge(0)
			.build();
	}

	public static String serialize(Object object) {
		return Base64.getUrlEncoder()
			.encodeToString(SerializationUtils.serialize(object));
	}

	public static <T> T deserialize(Cookie cookie, Class<T> cls) {
		return cls.cast(SerializationUtils.deserialize(
			Base64.getUrlDecoder().decode(cookie.getValue())
		));
	}
}


