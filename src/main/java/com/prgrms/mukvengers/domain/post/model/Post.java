package com.prgrms.mukvengers.domain.post.model;

import static javax.persistence.FetchType.*;
import static javax.persistence.GenerationType.*;
import static lombok.AccessLevel.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import com.prgrms.mukvengers.domain.Crew;
import com.prgrms.mukvengers.global.common.domain.BaseEntity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PROTECTED)
@Entity
@Getter
@Where(clause = "deleted = false")
@SQLDelete(sql = "UPDATE post set deleted = true where id=?")
public class Post extends BaseEntity {

	@Id
	@Column(name = "post_id")
	@GeneratedValue(strategy = IDENTITY)
	private Long id;

	@OneToOne(fetch = LAZY)
	@JoinColumn(name = "crew_id")
	private Crew crew;

	@Column(nullable = false)
	private Long leaderId;

	@Column(nullable = false, length = 500)
	private String content;

	@Builder
	private Post(Crew crew, Long leaderId, String content) {
		this.crew = crew;
		this.leaderId = leaderId;
		this.content = content;
	}
}
