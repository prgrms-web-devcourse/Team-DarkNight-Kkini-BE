package com.prgrms.mukvengers.domain.notification.model;

import static org.springframework.util.Assert.*;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import com.prgrms.mukvengers.domain.notification.model.vo.NotificationContent;
import com.prgrms.mukvengers.domain.notification.model.vo.NotificationType;
import com.prgrms.mukvengers.global.common.domain.BaseEntity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Where(clause = "deleted = false")
@SQLDelete(sql = "UPDATE notification set deleted = true where id=?")
@Table(indexes = @Index(name = "idx_receiver_id", columnList = "receiverId"))
public class Notification extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Embedded
	private NotificationContent content;

	@Column(nullable = false)
	private Boolean isRead;

	@Column(nullable = false)
	private Long receiverId;

	@Enumerated(EnumType.STRING)
	private NotificationType type;

	@Builder
	protected Notification(String content, Boolean isRead, Long receiverId,
		NotificationType type) {

		this.content = new NotificationContent(content);
		this.isRead = validateIsRead(isRead);
		this.receiverId = validateReceiverId(receiverId);
		this.type = validateType(type);
	}

	private Boolean validateIsRead(Boolean isRead) {
		notNull(isRead, "유효하지 않은 상태입니다.");
		return isRead;
	}

	private Long validateReceiverId(Long receiverId) {
		notNull(receiverId, "유효하지 않은 사용자입니다.");
		return receiverId;
	}

	private NotificationType validateType(NotificationType type) {
		notNull(type, "유효하지 않은 상태입니다.");
		return type;
	}

	public void read() {
		this.isRead = true;
	}
}