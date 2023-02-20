package com.prgrms.mukvengers.global.domain;

import static javax.persistence.GenerationType.*;
import static lombok.AccessLevel.*;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Size;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import com.prgrms.mukvengers.global.common.domain.BaseEntity;
import com.prgrms.mukvengers.global.utils.ValidateUtil;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = PROTECTED)
@Entity
@SQLDelete(sql = "UPDATE proposal SET deleted = true where id=?")
@Where(clause = "deleted=false")
public class Proposal extends BaseEntity {

	private static final int CONTENT_MAX_SIZE = 100;
	private static final String CONTENT_NOT_NULL = "신청서에 반드시 참여 목적을 작성하여야 합니다.";
	private static final String CONTENT_MAX_SIZE_OVER_MESSAGE = "신청서에 작성할 수 있는 최대 글자 수 넘겼습니다.";

	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "crew_id")
	private Crew crew;

	@Lob
	@Size(max = 100)
	private String content;

	private boolean checked;

	@Builder
	public Proposal(User user, Crew crew, String content, boolean checked) {
		this.user = user;
		this.crew = crew;
		this.content = validationContent(content);
		this.checked = checked;
	}

	private String validationContent(String content) {
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
