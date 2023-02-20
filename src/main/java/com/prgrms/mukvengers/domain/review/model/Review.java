package com.prgrms.mukvengers.domain.review.model;

import static javax.persistence.GenerationType.*;
import static lombok.AccessLevel.*;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import com.prgrms.mukvengers.domain.store.model.Store;
import com.prgrms.mukvengers.domain.user.model.User;
import com.prgrms.mukvengers.global.common.domain.BaseEntity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = PROTECTED)
@Where(clause = "deleted=false")
@SQLDelete(sql = "UPDATE review SET deleted = true where id=?")
@Entity
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

	private LocalDateTime promiseTime;

	@Size(max = 100)
	private String crewName;

	@Size(max = 255)
	private String content;

	@PositiveOrZero
	private Integer mannerPoint;

	@PositiveOrZero
	private Integer tastePoint;

	@Builder
	public Review(User reviewer, User reviewee, Store store, LocalDateTime promiseTime,
		String crewName, String content, Integer mannerPoint, Integer tastePoint) {
		this.reviewer = reviewer;
		this.reviewee = reviewee;
		this.store = store;
		this.promiseTime = promiseTime;
		this.crewName = crewName;
		this.content = content;
		this.mannerPoint = mannerPoint;
		this.tastePoint = tastePoint;
	}

}
