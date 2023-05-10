package com.prgrms.mukvengers.domain.notification.service;

import static com.prgrms.mukvengers.domain.notification.model.vo.NotificationType.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.prgrms.mukvengers.base.ServiceTest;
import com.prgrms.mukvengers.domain.notification.dto.response.NotificationResponses;
import com.prgrms.mukvengers.domain.notification.exception.NotificationNotFoundException;
import com.prgrms.mukvengers.domain.notification.model.Notification;
import com.prgrms.mukvengers.domain.user.exception.UserNotFoundException;
import com.prgrms.mukvengers.domain.user.repository.UserRepository;
import com.prgrms.mukvengers.utils.NotificationObjectProvider;

class NotificationServiceTest extends ServiceTest {

	@Autowired
	private NotificationService notificationService;

	@Autowired
	private UserRepository userRepository;

	@Test
	@DisplayName("[성공] 알림 구독을 진행한다.")
	public void subscribe() {
		//given
		String lastEventId = "";

		//when, then
		Assertions.assertDoesNotThrow(() ->
			notificationService.subscribe(savedUser1.getId(), lastEventId));
	}

	@Test
	@DisplayName("[성공] 알림 메세지를 전송한다.")
	public void send() {
		//given
		String lastEventId = "";
		notificationService.subscribe(savedUser1.getId(), lastEventId);

		//when, then
		Assertions.assertDoesNotThrow(() -> notificationService.send(savedUser1.getId(), "알림 발송!!!", INFO));
	}

	@Test
	@DisplayName("[성공] 알림 메세지를 저장한다.")
	public void save() {
		//given
		Notification noti = NotificationObjectProvider.createNotification("신촌으로 모여!", savedUser1.getId(),
			INFO);

		//when
		Notification savedOne = notificationService.save(noti.getReceiverId(), noti.getContent().getContent(),
			noti.getType());

		//then
		assertEquals(noti.getType(), savedOne.getType());
		assertEquals(noti.getReceiverId(), savedOne.getReceiverId());
		assertEquals(noti.getContent().getContent(), savedOne.getContent().getContent());
	}

	@Test
	@DisplayName("[성공] 알림 메세지를 읽음 처리한다.")
	public void read() {
		//given
		Notification notification = notificationService.save(savedUser1.getId(), "신촌으로 모여!", INFO);
		assertEquals(false, notification.getIsRead());

		//when
		notificationService.readNotification(notification.getId());

		//then
		assertEquals(true, notification.getIsRead());
	}

	@Test
	@DisplayName("[실패] 존재하지 않는 알림 ID를 조회할 경우 예외가 발생한다.")
	public void read_fail() {
		//given
		Notification notification = notificationService.save(savedUser1.getId(), "신촌으로 모여!", INFO);
		Long unknownId = 100L;

		//when & then
		assertThrows(NotificationNotFoundException.class, () -> notificationService.readNotification(unknownId));
	}

	@Test
	@DisplayName("[성공] 회원 ID로 알림을 조회할 수 있다.")
	public void findByReceiverId() {
		//given
		notificationService.save(savedUser1.getId(), "1번 알림", INFO);
		notificationService.save(savedUser1.getId(), "2번 알림", APPROVE);
		notificationService.save(savedUser1.getId(), "3번 알림", REJECT);

		//when
		NotificationResponses result = notificationService.findAllById(savedUser1.getId());

		//then
		assertEquals(3, result.notifications().size());
	}

	@Test
	@DisplayName("[실패] 존재하지 않는 회원 ID로 알림을 조회 시 예외가 발생한다.")
	public void findByReceiverId_fail() {
		//given
		notificationService.save(savedUser1.getId(), "1번 알림", INFO);

		Long unknownId = 100L;

		//when & then
		assertThrows(UserNotFoundException.class, () -> notificationService.findAllById(unknownId));
	}
}