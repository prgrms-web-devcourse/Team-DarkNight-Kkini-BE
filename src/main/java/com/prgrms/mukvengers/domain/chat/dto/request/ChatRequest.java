package com.prgrms.mukvengers.domain.chat.dto.request;

public record ChatRequest(
	MessageType type,
	Long userId,
	String content
) {
}

