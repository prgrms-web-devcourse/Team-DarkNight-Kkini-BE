package com.prgrms.mukvengers.domain.proposal.model;

import static javax.persistence.FetchType.*;
import static javax.persistence.GenerationType.*;
import static lombok.AccessLevel.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import com.prgrms.mukvengers.domain.crew.model.Crew;
import com.prgrms.mukvengers.domain.user.model.User;
import com.prgrms.mukvengers.global.common.domain.BaseEntity;
import com.prgrms.mukvengers.global.utils.ValidateUtil;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
@Where(clause = "deleted = false")
@SQLDelete(sql = "UPDATE proposal SET deleted = true where id=?")
public class Proposal extends BaseEntity {

	private static final int CONTENT_MAX_SIZE = 100;
	private static final String CONTENT_NOT_NULL = "신청서에 반드시 참여 목적을 작성하여야 합니다.";
	private static final String CONTENT_MAX_SIZE_OVER_MESSAGE = "신청서에 작성할 수 있는 최대 글자 수 넘겼습니다.";

	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Long id;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "crew_id")
	private Crew crew;

	@Column(nullable = false, length = 100)
	private String content;

	@Column(nullable = false)
	private boolean checked;

	@Builder
	protected Proposal(User user, Crew crew, String content, boolean checked) {
		this.user = user;
		this.crew = crew;
		this.content = validationContent(content);
		this.checked = checked;
	}

	private String validateContent(String content) {
		ValidateUtil.checkText(content, CONTENT_NOT_NULL);
		ValidateUtil.checkOverLength(content, CONTENT_MAX_SIZE, CONTENT_MAX_SIZE_OVER_MESSAGE);
		return content;
	}

	public void changeContent(String changeContent) {
		ValidateUtil.checkText(changeContent, CONTENT_NOT_NULL);
		ValidateUtil.checkOverLength(changeContent, CONTENT_MAX_SIZE, CONTENT_MAX_SIZE_OVER_MESSAGE);
		this.content = changeContent;
	}
}
