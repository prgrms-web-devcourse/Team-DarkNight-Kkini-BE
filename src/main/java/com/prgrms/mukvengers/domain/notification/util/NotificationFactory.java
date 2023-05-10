package com.prgrms.mukvengers.domain.notification.util;

import org.springframework.stereotype.Component;

import com.prgrms.mukvengers.domain.notification.model.Notification;
import com.prgrms.mukvengers.domain.notification.model.vo.NotificationType;

@Component
public class NotificationFactory {
	public Notification createNotification(Long receiverId, String content, NotificationType type) {
		return Notification.builder()
			.content(content)
			.isRead(false)
			.receiverId(receiverId)
			.type(type)
			.build();
	}
}
