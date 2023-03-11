package com.prgrms.mukvengers.domain.chat.model;

import static com.prgrms.mukvengers.global.utils.ValidateUtil.*;
import static javax.persistence.EnumType.*;
import static javax.persistence.GenerationType.*;
import static lombok.AccessLevel.*;
import static org.springframework.util.Assert.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

import com.prgrms.mukvengers.domain.chat.dto.request.MessageType;
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

	@Enumerated(STRING)
	private MessageType type;

	@Column(columnDefinition = "TEXT")
	private String content;

	@Builder
	protected Chat(Long crewId, Long userId, String content, MessageType type) {
		this.crewId = validateCrew(crewId);
		this.userId = validateUserId(userId);
		this.content = validateContent(content);
		this.type = type;
	}

	private Long validateUserId(Long userId) {
		notNull(userId, "유효하지 않은 사용자입니다.");
		return userId;
	}

	private Long validateCrew(Long crewId) {
		notNull(crewId, "유효하지 않은 밥모임입니다.");
		return crewId;
	}

	private String validateContent(String content) {
		checkText(content, "채팅을 입력해주세요.");
		return content;
	}
}
