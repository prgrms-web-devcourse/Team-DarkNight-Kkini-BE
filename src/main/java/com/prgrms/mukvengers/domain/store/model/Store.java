package com.prgrms.mukvengers.domain.store.model;

import static javax.persistence.GenerationType.*;
import static lombok.AccessLevel.*;
import static org.springframework.util.Assert.*;

import java.nio.charset.StandardCharsets;

import javax.persistence.AttributeConverter;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.ColumnTransformer;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.springframework.stereotype.Component;

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

	private static final Integer MAX_LATITUDE = 90;
	private static final Integer MIN_LATITUDE = -90;
	private static final Integer MAX_LONGITUDE = 180;
	private static final Integer MIN_LONGITUDE = -180;


	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Long id;

	@Convert(converter = PointConverter.class)
	@ColumnTransformer(write = "ST_PointFromText(?, 4326)", read = "ST_AsText(location)")
	@Column(nullable = false)
	private Point location;

	@Column(nullable = false)
	private String mapStoreId;

	@Builder
	protected Store(Point location, String mapStoreId) {
		validatePosition(location);
		validateLongitude(location);
		validateLatitude(location);
		validateMapStoreId(mapStoreId);

		this.location = location;
		this.mapStoreId = mapStoreId;
	}

	private void validatePosition(Point location) {
		notNull(location, "유효하지 않는 위치입니다.");
	}

	private void validateLatitude(Point location) {
		isTrue(location.getX() >= MIN_LATITUDE && location.getX() <= MAX_LATITUDE, "유효하지 않는 위도 값입니다.");
	}

	private void validateLongitude(Point location) {
		isTrue(location.getY() >= MIN_LONGITUDE && location.getY() <= MAX_LONGITUDE, "유효하지 않는 경도 값입니다.");
	}

	private void validateMapStoreId(String mapStoreId) {
		ValidateUtil.checkText(mapStoreId, "유효하지 않는 가게 아이디입니다.");
	}

	@Component
	public static class PointConverter implements AttributeConverter<Point, String> {
		static WKTReader wktReader = new WKTReader();

		@Override
		public String convertToDatabaseColumn(Point attribute) {
			return attribute.toText();
		}

		@Override
		public Point convertToEntityAttribute(String dbData) {
			try {
				String decoded = new String(dbData.getBytes(), StandardCharsets.UTF_8);

				return (Point)wktReader.read(decoded);
			} catch (ParseException e) {
				throw new IllegalArgumentException();
			}
		}
	}
}
