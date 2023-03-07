package com.prgrms.mukvengers.domain.chat.model;

import static com.prgrms.mukvengers.global.utils.ValidateUtil.*;
import static javax.persistence.FetchType.*;
import static javax.persistence.GenerationType.*;
import static lombok.AccessLevel.*;
import static org.springframework.util.Assert.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.prgrms.mukvengers.domain.crew.model.Crew;
import com.prgrms.mukvengers.global.common.domain.BaseEntity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Chat extends BaseEntity {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Long id;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "crew_id")
	private Crew crew;

	private String sender;

	@Column(columnDefinition = "TEXT")
	private String content;

	@Builder
	protected Chat(Crew crew, String sender, String content) {
		this.crew = validateCrew(crew);
		this.sender = validateSender(sender);
		this.content = validateContent(content);
	}

	private Crew validateCrew(Crew crew) {
		notNull(crew, "유효하지 않는 밥모임입니다.");
		return crew;
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
