package com.prgrms.mukvengers.domain.store.model;

import static javax.persistence.GenerationType.*;
import static lombok.AccessLevel.*;
import static org.springframework.util.Assert.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.locationtech.jts.geom.Point;
import org.springframework.util.Assert;

import com.prgrms.mukvengers.global.common.domain.BaseEntity;
import com.prgrms.mukvengers.global.utils.ValidateUtil;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
@Where(clause = "deleted = false")
@SQLDelete(sql = "UPDATE store set deleted = true where id=?")
public class Store extends BaseEntity {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Long id;

	@Column(nullable = false)
	private Point location;

	@Column(nullable = false)
	private String apiId;

	@Builder
	protected Store(Point location, String apiId) {
		this.location = location;
		this.apiId = apiId;
	}

	public void validatePosition(Point location) {
		notNull(location,"유효하지 않는 위치입니다.");
	}

	public void validateApiId(String apiId) {
		ValidateUtil.checkText(apiId,"유효하지 않는 가게 아이디 입니다.");
	}
}
