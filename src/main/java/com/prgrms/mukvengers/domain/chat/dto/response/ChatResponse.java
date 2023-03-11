package com.prgrms.mukvengers.domain.chat.dto.response;

import java.time.LocalDateTime;

import com.prgrms.mukvengers.domain.chat.dto.request.MessageType;

import lombok.Builder;

@Builder
public record ChatResponse(
	MessageType type,
	Long userId,
	String username,
	String profileImgUrl,
	LocalDateTime createdAt,
	String content
) {
}
