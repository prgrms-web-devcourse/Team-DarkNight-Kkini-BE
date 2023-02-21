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
import com.prgrms.mukvengers.domain.user.model.User;
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

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "user_id", referencedColumnName = "id")
	private User user;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "crew_id", referencedColumnName = "id")
	private Crew crew;

	@Column(nullable = false)
	private boolean blocked;

	@Column(nullable = false)
	private boolean ready;

	@Builder
	protected CrewMember(User user, Crew crew, boolean blocked, boolean ready) {
		this.user = user;
		this.crew = crew;
		this.blocked = blocked;
		this.ready = ready;
	}
}
