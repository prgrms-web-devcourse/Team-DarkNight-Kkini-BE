package com.prgrms.mukvengers.domain.crewmember.model;

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
import com.prgrms.mukvengers.global.common.domain.BaseEntity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
@Where(clause = "deleted = false")
@SQLDelete(sql = "UPDATE crew_member set deleted = true where id=?")
public class CrewMember extends BaseEntity {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Long id;

	@Column(nullable = false)
	private Long userId;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "crew_id", referencedColumnName = "id")
	private Crew crew;

	@Column(nullable = false)
	private boolean blocked;

	@Column(nullable = false)
	private boolean ready;

	@Column(nullable = false)
	private boolean isLeader;

	@Builder
	protected CrewMember(Long userId, Crew crew, boolean blocked, boolean ready, boolean isLeader) {
		this.userId = userId;
		this.crew = crew;
		this.blocked = blocked;
		this.ready = ready;
		this.isLeader = isLeader;
	}
}
