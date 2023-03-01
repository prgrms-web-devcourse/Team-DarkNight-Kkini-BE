package com.prgrms.mukvengers.domain.crew.model;

import static javax.persistence.EnumType.*;
import static javax.persistence.FetchType.*;
import static javax.persistence.GenerationType.*;
import static lombok.AccessLevel.*;
import static org.springframework.util.Assert.*;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.AttributeConverter;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.hibernate.annotations.ColumnTransformer;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.springframework.stereotype.Component;

import com.prgrms.mukvengers.domain.crew.model.vo.Category;
import com.prgrms.mukvengers.domain.crew.model.vo.Status;
import com.prgrms.mukvengers.domain.crewmember.model.CrewMember;
import com.prgrms.mukvengers.domain.store.model.Store;
import com.prgrms.mukvengers.global.common.domain.BaseEntity;
import com.prgrms.mukvengers.global.utils.ValidateUtil;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
@Where(clause = "deleted = false")
@SQLDelete(sql = "UPDATE crew set deleted = true where id=?")
public class Crew extends BaseEntity {

	private static final Integer MAX_LATITUDE = 90;
	private static final Integer MIN_LATITUDE = -90;
	private static final Integer MAX_LONGITUDE = 180;
	private static final Integer MIN_LONGITUDE = -180;


	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Long id;
  
	@OneToOne(fetch = LAZY)
	@JoinColumn(name = "store_id", referencedColumnName = "id")
	private Store store;

	@Column(nullable = false, length = 20)
	private String name;

	@Convert(converter = Store.PointConverter.class)
	@ColumnTransformer(write = "ST_PointFromText(?, 4326)", read = "ST_AsText(location)")
	@Column(nullable = false)
	private Point location;

	@Column(nullable = false)
	private Integer capacity;

	@Column(nullable = false, length = 255)
	@Enumerated(STRING)
	private Status status;

	@Column(nullable = false)
	private String content;

	@Enumerated(STRING)
	@Column(nullable = false)
	private Category category;

	@Column(nullable = false)
	private LocalDateTime promiseTime;

	@OneToMany(mappedBy = "crew")
	private List<CrewMember> crewMembers = new ArrayList<>();

	public void addCrewMember(CrewMember crewMember) {
		crewMembers.add(crewMember);
	}


	@Builder
	protected Crew(Store store, String name, Point location, Integer capacity, Status status,
		String content, Category category, LocalDateTime promiseTime) {

		validateStore(store);
		validateName(name);
		validatePosition(location);
		validateLatitude(location);
		validateLongitude(location);
		validateCapacity(capacity);
		validateStatus(status);
		validateCategory(category);

		this.store = store;
		this.name = name;
		this.location = location;
		this.capacity = capacity;
		this.status = status;
		this.content = content;
		this.category = category;
		this.promiseTime = promiseTime;
	}

	public Status changeStatus(String status) {
		this.status = validateStatus(Status.getStatus(status));

		return this.status;
	}

	private void validateStore(Store store) {
		notNull(store, "유효하지 않는 가게입니다");
	}

	private void validateName(String name) {
		ValidateUtil.checkText(name, "유효하지 않는 모임 이름입니다.");
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

	private void validateCapacity(Integer capacity) {
		isTrue(2 <= capacity && capacity <= 8, "유효하지 않는 인원 수 입니다");
	}

	private Status validateStatus(Status status) {
		notNull(status, "유효하지 않는 상태입니다.");
		return status;
	}

	private void validateCategory(Category category) {
		notNull(category, "유효하지 않는 카테고리입니다.");
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
