package com.prgrms.mukvengers.domain.store.model;

import static com.prgrms.mukvengers.global.utils.ValidateUtil.*;
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

import com.prgrms.mukvengers.global.common.domain.BaseEntity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
@Where(clause = "deleted = false")
@SQLDelete(sql = "UPDATE store set deleted = true where id=?")
public class Store extends BaseEntity {

	private static final Integer MAX_LATITUDE = 90;
	private static final Integer MIN_LATITUDE = -90;
	private static final Integer MAX_LONGITUDE = 180;
	private static final Integer MIN_LONGITUDE = -180;

	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Long id;

	@Column(nullable = false)
	private Point location;

	@Column(nullable = false)
	private String mapStoreId;

	@Builder
	protected Store(Point location, String mapStoreId) {
		this.location = validatePosition(location);
		this.mapStoreId = validateMapStoreId(mapStoreId);
	}

	private Point validatePosition(Point location) {
		notNull(location, "유효하지 않는 위치입니다.");

		validateLongitude(location);
		validateLatitude(location);

		return location;
	}

	private void validateLongitude(Point location) {
		isTrue(location.getX() >= MIN_LONGITUDE && location.getX() <= MAX_LONGITUDE, "유효하지 않는 경도 값입니다.");
	}

	private void validateLatitude(Point location) {
		isTrue(location.getY() >= MIN_LATITUDE && location.getY() <= MAX_LATITUDE, "유효하지 않는 위도 값입니다.");
	}

	private String validateMapStoreId(String mapStoreId) {
		checkText(mapStoreId, "유효하지 않는 가게 아이디입니다.");
		return mapStoreId;
	}

}
