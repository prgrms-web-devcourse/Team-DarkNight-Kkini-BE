package com.prgrms.mukvengers.global.utils;

import static lombok.AccessLevel.*;

import java.util.Locale;

import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.util.Assert;

import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public class MessageUtil {

	private static final String MESSAGE_ALT = "오류 메시지를 찾지 못했습니다.";
	private static final String MESSAGE_SOURCE_ACCESSOR_NULL = "MessageSourceAccessor가 등록되지 않았습니다.";
	private static MessageSourceAccessor messageSourceAccessor;

	public static String getMessage(String key) {
		Assert.notNull(messageSourceAccessor, MESSAGE_SOURCE_ACCESSOR_NULL);
		return messageSourceAccessor.getMessage(key, MESSAGE_ALT, Locale.KOREA);
	}

	public static void setMessageSourceAccessor(MessageSourceAccessor messageSourceAccessor) {
		MessageUtil.messageSourceAccessor = messageSourceAccessor;
	}
}
