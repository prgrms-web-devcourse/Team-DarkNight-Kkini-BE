package com.prgrms.mukvengers.domain.notification.dto.response;

import java.time.LocalDateTime;

public record NotificationResponse(
	Long id,
	String type,
	String content,
	LocalDateTime createdAt,
	boolean isRead
) {
}
