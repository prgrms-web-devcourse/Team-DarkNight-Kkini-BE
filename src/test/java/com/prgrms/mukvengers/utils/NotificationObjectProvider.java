package com.prgrms.mukvengers.utils;

import com.prgrms.mukvengers.domain.notification.model.Notification;
import com.prgrms.mukvengers.domain.notification.model.vo.NotificationType;

public class NotificationObjectProvider {

	public static Notification createNotification(String content, Long receiverId, NotificationType type) {
		return Notification.builder()
			.content(content)
			.isRead(false)
			.receiverId(receiverId)
			.type(type)
			.build();
	}

	public static Notification createNotification(String content, Long receiverId) {
		return Notification.builder()
			.content(content)
			.isRead(false)
			.receiverId(receiverId)
			.type(NotificationType.INFO)
			.build();
	}
}
