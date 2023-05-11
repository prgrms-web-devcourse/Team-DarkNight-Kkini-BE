package com.prgrms.mukvengers.domain.notification.repository;

import static com.prgrms.mukvengers.domain.notification.model.vo.NotificationType.*;
import static com.prgrms.mukvengers.utils.NotificationObjectProvider.*;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.prgrms.mukvengers.base.RepositoryTest;
import com.prgrms.mukvengers.domain.notification.model.Notification;

class NotificationRepositoryTest extends RepositoryTest {

	@Test
	@DisplayName("[성공] 회원 ID로 해당 회원에게 발송된 알림을 전부 조회할 수 있다")
	void findNotification_receiverId_success() {
		//given
		Notification notification1 = createNotification("알림1", savedUser1.getId(), INFO);
		Notification notification2 = createNotification("거절당했어용", savedUser1.getId(), REJECT);
		notificationRepository.save(notification1);
		notificationRepository.save(notification2);

		//when
		List<Notification> result = notificationRepository.findAllByReceiverId(savedUser1Id);

		//then
		Assertions.assertEquals(2, result.size());
	}
}