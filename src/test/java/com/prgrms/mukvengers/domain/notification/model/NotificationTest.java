package com.prgrms.mukvengers.domain.notification.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullSource;

import com.prgrms.mukvengers.domain.notification.exception.InvalidNotificationContentException;
import com.prgrms.mukvengers.domain.notification.model.vo.NotificationContent;
import com.prgrms.mukvengers.domain.notification.model.vo.NotificationType;
import com.prgrms.mukvengers.domain.user.model.User;
import com.prgrms.mukvengers.utils.UserObjectProvider;

class NotificationTest {

	@ParameterizedTest
	@NullSource
	@EmptySource
	@DisplayName("알림 내용이 공백일 경우 커스텀 예외가 발생한다.")
	public void createNotificationContent_fail(String content) {
		//given

		//when, then
		Assertions.assertThrows(InvalidNotificationContentException.class, () -> new NotificationContent(content));
	}

	@Test
	@DisplayName("알림 내용이 50글자 이상일 경우 커스텀 예외가 발생한다.")
	public void createNotificationContent_fail2() {
		//given

		//when, then
		Assertions.assertThrows(InvalidNotificationContentException.class,
			() -> new NotificationContent("hh".repeat(30)));
	}

	@Test
	@DisplayName("알림 내용이 성공적으로 생성된다.")
	public void createNotificationContent_success() {
		//given

		//when, then
		Assertions.assertDoesNotThrow(() -> new NotificationContent("밥먹자"));
	}

	@Test
	@DisplayName("Type 정보를 가진 알림이 성공적으로 생성된다.")
	public void createNotification_success() {
		//given
		User user = UserObjectProvider.createUser();
		String content = "신청서 도착했어용";
		//when, then

		Assertions.assertDoesNotThrow(() -> new Notification(content, false, user, NotificationType.CONNECT));
		Assertions.assertDoesNotThrow(() -> new Notification(content, false, user, NotificationType.APPROVE));
		Assertions.assertDoesNotThrow(() -> new Notification(content, false, user, NotificationType.REJECT));
	}

	@ParameterizedTest
	@NullSource
	@DisplayName("Type 정보가 없는 경우 예외가 발생한다.")
	public void invalidType_fail(NotificationType type) {
		//given
		User user = UserObjectProvider.createUser();
		String content = "신청서 도착했어용";
		//when, then

		Assertions.assertThrows(IllegalArgumentException.class,
			() -> new Notification(content, false, user, type));
	}

	@ParameterizedTest
	@NullSource
	@DisplayName("receiver 정보가 없는 경우 예외가 발생한다.")
	public void invalidReceiver_fail(User receiver) {
		//given
		String content = "신청서 도착했어용";

		//when, then
		Assertions.assertThrows(IllegalArgumentException.class,
			() -> new Notification(content, false, receiver, NotificationType.CONNECT));

		Assertions.assertThrows(IllegalArgumentException.class,
			() -> new Notification(content, false, receiver, NotificationType.APPROVE));

		Assertions.assertThrows(IllegalArgumentException.class,
			() -> new Notification(content, false, receiver, NotificationType.REJECT));
	}

}