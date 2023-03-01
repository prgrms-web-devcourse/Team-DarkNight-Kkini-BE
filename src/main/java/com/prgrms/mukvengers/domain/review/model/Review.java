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

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import com.prgrms.mukvengers.domain.store.model.Store;
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
	@JoinColumn(name = "store_id", referencedColumnName = "id")
	private Store store;

	@Column(nullable = false)
	private LocalDateTime promiseTime;

	@Column(nullable = false, length = 20)
	private String crewName;

	@Column
	private String content;

	@Column(nullable = false)
	private Integer mannerPoint;

	@Column
	private Integer tastePoint;

	@Builder
	protected Review(User reviewer, User reviewee, Store store, LocalDateTime promiseTime,
		String crewName, String content, Integer mannerPoint, Integer tastePoint) {

		this.reviewer = validateUser(reviewer);
		this.reviewee = validateUser(reviewee);
		this.store = validateStore(store);
		this.promiseTime = validatePromiseTime(promiseTime);
		this.crewName = validateCrewName(crewName);
		this.content = content;
		this.mannerPoint = validateMannerPoint(mannerPoint);
		this.tastePoint = tastePoint;
	}

	private LocalDateTime validatePromiseTime(LocalDateTime promiseTime) {
		notNull(promiseTime, "유효하지 않는 약속시간입니다.");
		return promiseTime;
	}

	private User validateUser(User user) {
		notNull(user, "유효하지 않는 User");
		return user;
	}

	private Store validateStore(Store store) {
		notNull(store, "유효하지 않는 가게입니다.");
		return store;
	}

	private String validateCrewName(String crewName) {
		hasText(crewName, "유효하지 않는 크루명입니다.");
		return crewName;
	}

	private Integer validateMannerPoint(Integer mannerPoint) {
		notNull(mannerPoint, "유효하지 않는 매너점수입니다.");
		return mannerPoint;
	}
}
