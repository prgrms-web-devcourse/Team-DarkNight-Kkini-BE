package com.prgrms.mukvengers.domain.notification.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullSource;

import com.prgrms.mukvengers.domain.notification.exception.InvalidNotificationContentException;
import com.prgrms.mukvengers.domain.notification.model.vo.NotificationContent;
import com.prgrms.mukvengers.domain.notification.model.vo.NotificationType;

class NotificationTest {

	@ParameterizedTest
	@NullSource
	@EmptySource
	@DisplayName("[실패] 알림 내용이 공백일 경우 커스텀 예외가 발생한다.")
	public void createNotificationContent_fail(String content) {
		//given

		//when, then
		assertThrows(InvalidNotificationContentException.class, () -> new NotificationContent(content));
	}

	@Test
	@DisplayName("[실패] 알림 내용이 50글자 이상일 경우 커스텀 예외가 발생한다.")
	public void createNotificationContent_fail2() {
		//given

		//when, then
		assertThrows(InvalidNotificationContentException.class,
			() -> new NotificationContent("hh".repeat(30)));
	}

	@Test
	@DisplayName("[성공] 알림 내용이 성공적으로 생성된다.")
	public void createNotificationContent_success() {
		//given

		//when, then
		assertDoesNotThrow(() -> new NotificationContent("밥먹자"));
	}

	@Test
	@DisplayName("[성공] Type 정보를 가진 알림이 성공적으로 생성된다.")
	public void createNotification_success() {
		//given
		String content = "신청서 도착했어용";
		//when, then

		assertDoesNotThrow(() -> new Notification(content, false, 1L, NotificationType.CONNECT));
		assertDoesNotThrow(() -> new Notification(content, false, 1L, NotificationType.APPROVE));
		assertDoesNotThrow(() -> new Notification(content, false, 1L, NotificationType.REJECT));
	}

	@ParameterizedTest
	@NullSource
	@DisplayName("[실패] Type 정보가 없는 경우 예외가 발생한다.")
	public void invalidType_fail(NotificationType type) {
		//given
		String content = "신청서 도착했어용";
		//when, then

		assertThrows(IllegalArgumentException.class,
			() -> new Notification(content, false, 1L, type));
	}

	@ParameterizedTest
	@NullSource
	@DisplayName("[실패] receiver 정보가 없는 경우 예외가 발생한다.")
	public void invalidReceiver_fail(Long receiverId) {
		//given
		String content = "신청서 도착했어용";

		//when, then
		assertThrows(IllegalArgumentException.class,
			() -> new Notification(content, false, receiverId, NotificationType.CONNECT));

		assertThrows(IllegalArgumentException.class,
			() -> new Notification(content, false, receiverId, NotificationType.APPROVE));

		assertThrows(IllegalArgumentException.class,
			() -> new Notification(content, false, receiverId, NotificationType.REJECT));
	}

	@Test
	@DisplayName("[성공] 알림 메세지를 읽음 처리한다.")
	public void read() {
		//given
		String content = "밥먹자";
		Notification notification = new Notification(content, false, 1L, NotificationType.CONNECT);
		assertEquals(false, notification.getIsRead());

		//when
		notification.read();

		//then
		assertEquals(true, notification.getIsRead());
	}
}