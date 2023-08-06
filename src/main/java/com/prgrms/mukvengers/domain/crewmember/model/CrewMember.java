package com.prgrms.mukvengers.domain.crewmember.model;

import static com.prgrms.mukvengers.domain.crewmember.model.vo.CrewMemberRole.*;
import static javax.persistence.EnumType.*;
import static javax.persistence.FetchType.*;
import static javax.persistence.GenerationType.*;
import static lombok.AccessLevel.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.prgrms.mukvengers.domain.crewmember.exception.NotLeaderException;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import com.prgrms.mukvengers.domain.crew.model.Crew;
import com.prgrms.mukvengers.domain.crewmember.model.vo.CrewMemberRole;
import com.prgrms.mukvengers.global.base.domain.BaseEntity;

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

	@Enumerated(STRING)
	@Column(nullable = false)
	private CrewMemberRole crewMemberRole;

	@Builder
	protected CrewMember(Long userId, Crew crew, CrewMemberRole crewMemberRole) {
		this.userId = userId;
		this.crew = crew;
		this.crewMemberRole = crewMemberRole;
	}

	public void blockMember() {
		this.crewMemberRole = BLOCKED;
	}

	public boolean isLeader() {
		return this.crewMemberRole == LEADER;
	}

	public void isNotLeader() {
		if (!this.getCrewMemberRole().equals(CrewMemberRole.LEADER)) {
			throw new NotLeaderException(CrewMemberRole.LEADER);
		}
	}

	public boolean isMember() {
		return this.crewMemberRole == MEMBER;
	}

	public boolean isBlocked() {
		return this.crewMemberRole == BLOCKED;
	}

}
