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
	private String placeId;

	@Column(nullable = false)
	private String placeName;

	@Column(nullable = false)
	private String categories;

	@Column(nullable = false)
	private String roadAddressName;

	@Column(nullable = false)
	private String photoUrls;

	@Column(nullable = false)
	private String kakaoPlaceUrl;

	@Column(nullable = false)
	private String phoneNumber;

	@Builder
	protected Store(Point location, String placeId, String placeName, String categories, String roadAddressName,
		String photoUrls, String kakaoPlaceUrl, String phoneNumber) {
		this.location = validatePosition(location);
		this.placeId = validatePlaceId(placeId);
		this.placeName = validatePlaceName(placeName);
		this.categories = validateCategories(categories);
		this.roadAddressName = validateRoadAddressName(roadAddressName);
		this.photoUrls = validatePhotoUrls(photoUrls);
		this.kakaoPlaceUrl = validateKakaoPlaceUrl(kakaoPlaceUrl);
		this.phoneNumber = validatePhoneNumber(phoneNumber);
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

	private String validatePlaceId(String placeId) {
		checkText(placeId, "유효하지 않는 가게 아이디입니다.");
		return placeId;
	}

	private String validatePlaceName(String placeName) {
		checkText(placeName, "유효하지 않는 가게 이름입니다.");
		return placeName;
	}

	private String validateCategories(String categories) {
		checkText(categories, "유효하지 않는 가게 카테고리입니다.");
		return categories;
	}

	private String validateRoadAddressName(String roadAddressName) {
		checkText(roadAddressName, "유효하지 않는 가게 도로명 주소입니다.");
		return roadAddressName;
	}

	private String validatePhotoUrls(String photoUrls) {
		checkText(photoUrls, "유효하지 않는 가게 사진 URL입니다.");
		return photoUrls;
	}

	private String validateKakaoPlaceUrl(String kakaoPlaceUrl) {
		checkText(kakaoPlaceUrl, "유효하지 않는 가게 상세페이지 URL입니다.");
		return kakaoPlaceUrl;
	}

	private String validatePhoneNumber(String phoneNumber) {
		checkText(phoneNumber, "유효하지 않는 가게 전화번호입니다.");
		return phoneNumber;
	}

}
