package com.prgrms.mukvengers.domain.notification.dto.response;

import java.util.List;

public record NotificationResponses(
	List<NotificationResponse> notifications,
	long unReadCount
) {

	public NotificationResponses(List<NotificationResponse> notifications, long unReadCount) {
		this.notifications = notifications;
		this.unReadCount = unReadCount;
	}
}
