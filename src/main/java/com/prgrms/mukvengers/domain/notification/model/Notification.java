package com.prgrms.mukvengers.domain.notification.model;

import static org.springframework.util.Assert.*;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import com.prgrms.mukvengers.domain.notification.model.vo.NotificationContent;
import com.prgrms.mukvengers.domain.notification.model.vo.NotificationType;
import com.prgrms.mukvengers.domain.user.model.User;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Where(clause = "deleted = false")
@SQLDelete(sql = "UPDATE crew set deleted = true where id=?")
public class Notification {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "notification_id")
	private Long id;

	@Embedded
	private NotificationContent content;

	@Column(nullable = false)
	private Boolean isRead;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	@OnDelete(action = OnDeleteAction.CASCADE)
	private User receiver;

	@Enumerated(EnumType.STRING)
	private NotificationType type;

	@Builder
	protected Notification(String content, Boolean isRead, User receiver,
		NotificationType type) {

		this.content = new NotificationContent(content);
		this.isRead = validateIsRead(isRead);
		this.receiver = validateUser(receiver);
		this.type = validateType(type);
	}

	private Boolean validateIsRead(Boolean isRead) {
		notNull(isRead, "유효하지 않은 상태입니다.");
		return isRead;
	}

	private User validateUser(User user) {
		notNull(user, "유효하지 않은 사용자입니다.");
		return user;
	}

	private NotificationType validateType(NotificationType type) {
		notNull(type, "유효하지 않은 상태입니다.");
		return type;
	}
}