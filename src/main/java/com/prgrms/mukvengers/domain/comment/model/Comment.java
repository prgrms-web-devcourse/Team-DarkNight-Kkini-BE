package com.prgrms.mukvengers.domain.comment.model;

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

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
@Where(clause = "deleted = false")
@SQLDelete(sql = "UPDATE comment set deleted = true where id=?")
public class Comment extends BaseEntity {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Long id;

	// @ManyToOne(fetch = LAZY)
	// @JoinColumn(name = "post_id")
	// private Post post;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "crew_id")
	private Crew crew;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@Column(nullable = false, length = 500)
	private String content;


	@Builder
	protected Comment(Crew crew, User user, String content) {
		this.crew = crew;
		this.user = user;
		this.content = content;
	}
}
