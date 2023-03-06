package com.prgrms.mukvengers.domain.review.model;

import static javax.persistence.GenerationType.*;
import static lombok.AccessLevel.*;
import static org.springframework.util.Assert.*;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.DynamicInsert;
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
@SQLDelete(sql = "UPDATE review SET deleted = true where id=?")
@DynamicInsert
public class Review extends BaseEntity {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "reviewer", referencedColumnName = "id")
	private User reviewer;

	@ManyToOne
	@JoinColumn(name = "reviewee", referencedColumnName = "id")
	private User reviewee;

	@ManyToOne
	@JoinColumn(name = "crew_id", referencedColumnName = "id")
	private Crew crew;

	@Column(nullable = false)
	private LocalDateTime promiseTime;

	@Column
	private String content;

	@Column(nullable = false)
	private Integer mannerPoint;

	@Column
	private Integer tastePoint;

	@Builder
	protected Review(User reviewer, User reviewee, Crew crew, LocalDateTime promiseTime,
		String content, Integer mannerPoint, Integer tastePoint) {

		this.reviewer = validateUser(reviewer);
		this.reviewee = validateUser(reviewee);
		this.crew = validateCrew(crew);
		this.promiseTime = validatePromiseTime(promiseTime);
		this.content = content;
		this.mannerPoint = validateMannerPoint(mannerPoint);
		this.tastePoint =  validateTastePoint(tastePoint);
	}

	private LocalDateTime validatePromiseTime(LocalDateTime promiseTime) {
		notNull(promiseTime, "유효하지 않는 약속시간입니다.");
		return promiseTime;
	}

	private User validateUser(User user) {
		notNull(user, "유효하지 않는 User");
		return user;
	}

	private Crew validateCrew(Crew crew) {
		notNull(crew, "유효하지 않는 밥모임입니다.");
		return crew;
	}

	private Integer validateMannerPoint(Integer mannerPoint) {
		notNull(mannerPoint, "유효하지 않는 매너점수입니다.");
		return mannerPoint;
	}

	private Integer validateTastePoint(Integer tastePoint) {
		notNull(tastePoint, "유효하지 않는 맛 점수입니다.");
		return tastePoint;
	}
}
