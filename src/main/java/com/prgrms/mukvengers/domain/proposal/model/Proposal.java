package com.prgrms.mukvengers.domain.proposal.model;

import static javax.persistence.EnumType.*;
import static javax.persistence.FetchType.*;
import static javax.persistence.GenerationType.*;
import static lombok.AccessLevel.*;
import static org.springframework.util.Assert.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import com.prgrms.mukvengers.domain.proposal.model.vo.ProposalStatus;
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

	private Long leaderId;

	private Long crewId;

	@Column(nullable = false, length = 100)
	private String content;

	@Column(nullable = false)
	@Enumerated(STRING)
	private ProposalStatus status;

	@Builder
	protected Proposal(User user, Long leaderId, Long crewId, String content) {
		this.user = validateUser(user);
		this.leaderId = validateId(leaderId);
		this.crewId = validateId(crewId);
		this.content = validateContent(content);
		this.status = ProposalStatus.WAITING;
	}

	private User validateUser(User user) {
		notNull(user, "유효하지 않는 User");
		return user;
	}

	private Long validateId(Long id) {
		notNull(id, "유효하지 않는 Id");
		return id;
	}

	private String validateContent(String content) {
		ValidateUtil.checkText(content, CONTENT_NOT_NULL);
		ValidateUtil.checkOverLength(content, CONTENT_MAX_SIZE, CONTENT_MAX_SIZE_OVER_MESSAGE);
		return content;
	}

}