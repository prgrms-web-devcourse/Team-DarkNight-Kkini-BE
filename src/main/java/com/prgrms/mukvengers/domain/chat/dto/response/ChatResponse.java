package com.prgrms.mukvengers.domain.chat.dto.response;

import java.time.LocalDateTime;

import com.prgrms.mukvengers.domain.chat.dto.request.MessageType;

import lombok.Builder;

@Builder
public record ChatResponse(
	Long userId,
	String username,
	String profileImgUrl,
	MessageType type,
	LocalDateTime createdAt,
	String content
) {
}
