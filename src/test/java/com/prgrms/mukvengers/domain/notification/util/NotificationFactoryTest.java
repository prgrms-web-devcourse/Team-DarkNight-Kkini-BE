package com.prgrms.mukvengers.domain.notification.util;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.prgrms.mukvengers.domain.notification.model.Notification;
import com.prgrms.mukvengers.domain.notification.model.vo.NotificationType;

@SpringBootTest
class NotificationFactoryTest {

	@Autowired
	private NotificationFactory factory;

	@Test
	@DisplayName("[성공] 알림이 정상 생성된다.")
	public void create() {
		//given
		Long receiverId = 1L;
		String content = "알림이요";
		NotificationType type = NotificationType.INFO;

		//when
		Notification notification = factory.createNotification(receiverId, content, type);

		//then
		assertEquals(receiverId, notification.getReceiverId());
		assertEquals(content, notification.getContent().getContent());
		assertEquals(type, notification.getType());
	}
}