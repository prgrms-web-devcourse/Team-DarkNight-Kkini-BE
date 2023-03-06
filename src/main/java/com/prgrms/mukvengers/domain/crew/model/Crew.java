package com.prgrms.mukvengers.domain.crew.model;

import static com.prgrms.mukvengers.global.utils.ValidateUtil.*;
import static javax.persistence.EnumType.*;
import static javax.persistence.FetchType.*;
import static javax.persistence.GenerationType.*;
import static lombok.AccessLevel.*;
import static org.springframework.util.Assert.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.locationtech.jts.geom.Point;

import com.prgrms.mukvengers.domain.crew.model.vo.Category;
import com.prgrms.mukvengers.domain.crew.model.vo.CrewStatus;
import com.prgrms.mukvengers.domain.crewmember.model.CrewMember;
import com.prgrms.mukvengers.domain.store.model.Store;
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

	private static final Integer MAX_LATITUDE = 90;
	private static final Integer MIN_LATITUDE = -90;
	private static final Integer MAX_LONGITUDE = 180;
	private static final Integer MIN_LONGITUDE = -180;

	@OneToMany(mappedBy = "crew")
	private final List<CrewMember> crewMembers = new ArrayList<>();

	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Long id;

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
	private CrewStatus status;

	@Column(nullable = false)
	private String content;

	@Enumerated(STRING)
	@Column(nullable = false)
	private Category category;

	@Column(nullable = false)
	private LocalDateTime promiseTime;

	@Builder
	protected Crew(Store store, String name, Point location, Integer capacity,
		String content, Category category, LocalDateTime promiseTime) {

		this.store = validateStore(store);
		this.name = validateName(name);
		this.location = validatePosition(location);
		this.capacity = validateCapacity(capacity);
		this.status = CrewStatus.RECRUITING;
		this.category = validateCategory(category);
		this.content = validateContent(content);
		this.promiseTime = validatePromiseTime(promiseTime);
	}

	public void addCrewMember(CrewMember crewMember) {
		crewMembers.add(crewMember);
	}

	public void changeStatus(String status) {
		this.status = validateStatus(CrewStatus.of(status));
	}

	private Store validateStore(Store store) {
		notNull(store, "유효하지 않는 가게입니다");
		return store;
	}

	private String validateName(String name) {
		checkText(name, "유효하지 않는 모임 이름입니다.");
		return name;
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

	private Integer validateCapacity(Integer capacity) {
		isTrue(2 <= capacity && capacity <= 8, "유효하지 않는 인원 수 입니다");
		return capacity;
	}

	private CrewStatus validateStatus(CrewStatus status) {
		notNull(status, "유효하지 않는 상태입니다.");
		return status;
	}

	private Category validateCategory(Category category) {
		notNull(category, "유효하지 않는 카테고리입니다.");
		return category;
	}

	private String validateContent(String content) {
		notNull(content, "유효하지 않는 콘텐츠입니다..");
		return content;
	}

	private LocalDateTime validatePromiseTime(LocalDateTime promiseTime) {
		notNull(promiseTime, "유효하지 않는 약속시간입니다.");
		return promiseTime;
	}
}
