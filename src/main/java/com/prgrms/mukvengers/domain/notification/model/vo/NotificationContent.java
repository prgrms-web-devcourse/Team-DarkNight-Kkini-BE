package com.prgrms.mukvengers.domain.notification.model.vo;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import com.prgrms.mukvengers.domain.notification.exception.InvalidNotificationContentException;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NotificationContent {

	private static final int MAX_LENGTH = 50;

	@Column(nullable = false, length = MAX_LENGTH)
	private String content;

	public NotificationContent(String content) {
		if (validateContent(content)) {
			throw new InvalidNotificationContentException(content);
		}

		this.content = content;
	}

	private boolean validateContent(String content) {
		return Objects.isNull(content) || content.isBlank() ||
			content.length() > MAX_LENGTH;
	}
}
