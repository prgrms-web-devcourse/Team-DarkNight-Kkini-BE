package com.prgrms.mukvengers.domain.chat.model;

import static com.prgrms.mukvengers.global.utils.ValidateUtil.*;
import static javax.persistence.GenerationType.*;
import static lombok.AccessLevel.*;
import static org.springframework.util.Assert.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

import com.prgrms.mukvengers.global.common.domain.BaseEntity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
@Table(indexes = @Index(name = "IDX_CREWID", columnList = "crewId"))
public class Chat extends BaseEntity {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Long id;

	private Long crewId;

	private Long userId;

	private String sender;

	@Column(columnDefinition = "TEXT")
	private String content;

	@Builder
	protected Chat(Long crewId, Long userId, String sender, String content) {
		this.crewId = validateCrew(crewId);
		this.userId = validateUserId(userId);
		this.sender = validateSender(sender);
		this.content = validateContent(content);
	}

	private Long validateUserId(Long userId) {
		notNull(userId, "유효하지 않은 사용자입니다.");
		return userId;
	}

	private Long validateCrew(Long crewId) {
		notNull(crewId, "유효하지 않은 밥모임입니다.");
		return crewId;
	}

	private String validateSender(String sender) {
		checkText(sender, "유효하지 않는 사용자입니다.");
		return sender;
	}

	private String validateContent(String content) {
		checkText(content, "채팅을 입력해주세요.");
		return content;
	}
}
