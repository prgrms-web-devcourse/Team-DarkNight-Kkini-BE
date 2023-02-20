package com.prgrms.mukvengers.global.utils;

import static lombok.AccessLevel.*;

import java.util.Objects;

import org.springframework.util.Assert;

import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public class ValidateUtil {

	public static void checkText(String text, String message) {
		Assert.hasText(text, message);
	}

	public static void checkOverLength(String text, int length, String message) {
		if (Objects.nonNull(text) && text.length() > length) {
			throw new IllegalArgumentException(message);
		}
	}
}
