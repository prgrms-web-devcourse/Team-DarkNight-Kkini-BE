package com.prgrms.mukvengers.domain.crew.model;

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
import javax.persistence.OneToOne;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.locationtech.jts.geom.Point;

import com.prgrms.mukvengers.domain.crew.model.vo.Category;
import com.prgrms.mukvengers.domain.crew.model.vo.Status;
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
@SQLDelete(sql = "UPDATE crew set deleted = true where id=?")
public class Crew extends BaseEntity {

	@Id
	@GeneratedValue(strategy = AUTO)
	private Long id;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "user_id", referencedColumnName = "id")
	private User user;

	@OneToOne(fetch = LAZY)
	@JoinColumn(name = "store_id", referencedColumnName = "id")
	private Store store;

	@Column(nullable = false, length = 20)
	private String name;

	@Column(nullable = false)
	private Point location;

	@Column(nullable = false)
	private Integer capacity;

	@Column(nullable = false, length = 255)
	@Enumerated(STRING)
	private Status status;

	@Column(nullable = false)
	private String content;

	@Column(nullable = false, length = 255)
	@Enumerated(STRING)
	private Category category;

	@Builder
	protected Crew(User user, Store store, String name, Point location, Integer capacity, Status status, String content,
		Category category) {
		this.user = user;
		this.store = store;
		this.name = name;
		this.location = location;
		this.capacity = capacity;
		this.status = status;
		this.content = content;
		this.category = category;
	}
}
