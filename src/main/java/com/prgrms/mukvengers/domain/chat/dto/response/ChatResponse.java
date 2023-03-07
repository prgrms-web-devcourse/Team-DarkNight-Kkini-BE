package com.prgrms.mukvengers.domain.chat.dto.response;

import java.time.LocalDateTime;

public record ChatResponse(
	String content,
	String sender,
	LocalDateTime createdAt
) {
}
