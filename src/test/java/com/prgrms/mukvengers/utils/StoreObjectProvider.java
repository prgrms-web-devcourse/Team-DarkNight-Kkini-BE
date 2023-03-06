package com.prgrms.mukvengers.utils;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;

import com.prgrms.mukvengers.domain.store.dto.request.CreateStoreRequest;
import com.prgrms.mukvengers.domain.store.model.Store;

public class StoreObjectProvider {

	private final static GeometryFactory STORE_GF = new GeometryFactory();
	private final static double STORE_LONGITUDE = -147.4654321321;
	private final static double STORE_LATITUDE = 35.75413579;
	private final static String STORE_PLACE_ID = "16618597";
	private final static String STORE_PLACE_NAME = "김춘배 고깃집";
	private final static String STORE_CATEGORIES = "음식점 > 한식";
	private final static String STORE_ROAD_ADDRESS_NAME = "도봉구 도봉로";
	private final static String STORE_PHOTO_URLS = "'https://~', 'https://~'";
	private final static String STORE_KAKAO_PLACE_URL = "http://place.map.kakao.com/24199893";
	private final static String STORE_PHONE_NUMBER = "02-017-3495";

	public static Store createStore() {

		Point location = STORE_GF.createPoint(new Coordinate(STORE_LONGITUDE, STORE_LATITUDE));

		return Store.builder()
			.location(location)
			.placeId(STORE_PLACE_ID)
			.placeName(STORE_PLACE_NAME)
			.categories(STORE_CATEGORIES)
			.roadAddressName(STORE_ROAD_ADDRESS_NAME)
			.photoUrls(STORE_PHOTO_URLS)
			.kakaoPlaceUrl(STORE_KAKAO_PLACE_URL)
			.phoneNumber(STORE_PHONE_NUMBER)
			.build();

	}

	public static void createStore(Point location) {

		Store.builder()
			.location(location)
			.placeId(STORE_PLACE_ID)
			.placeName(STORE_PLACE_NAME)
			.categories(STORE_CATEGORIES)
			.roadAddressName(STORE_ROAD_ADDRESS_NAME)
			.photoUrls(STORE_PHOTO_URLS)
			.kakaoPlaceUrl(STORE_KAKAO_PLACE_URL)
			.phoneNumber(STORE_PHONE_NUMBER)
			.build();

	}

	public static void createStore(double longitude, double latitude) {

		Point location = STORE_GF.createPoint(new Coordinate(longitude, latitude));

		Store.builder()
			.location(location)
			.placeId(STORE_PLACE_ID)
			.placeName(STORE_PLACE_NAME)
			.categories(STORE_CATEGORIES)
			.roadAddressName(STORE_ROAD_ADDRESS_NAME)
			.photoUrls(STORE_PHOTO_URLS)
			.kakaoPlaceUrl(STORE_KAKAO_PLACE_URL)
			.phoneNumber(STORE_PHONE_NUMBER)
			.build();

	}

	public static Store createStore(String placeId) {

		Point location = STORE_GF.createPoint(new Coordinate(STORE_LONGITUDE, STORE_LATITUDE));

		return Store.builder()
			.location(location)
			.placeId(placeId)
			.placeName(STORE_PLACE_NAME)
			.categories(STORE_CATEGORIES)
			.roadAddressName(STORE_ROAD_ADDRESS_NAME)
			.photoUrls(STORE_PHOTO_URLS)
			.kakaoPlaceUrl(STORE_KAKAO_PLACE_URL)
			.phoneNumber(STORE_PHONE_NUMBER)
			.build();

	}

	public static CreateStoreRequest getCreateStoreRequest(String placeId) {
		return new CreateStoreRequest(
			STORE_LONGITUDE,
			STORE_LATITUDE,
			placeId,
			STORE_PLACE_NAME,
			STORE_CATEGORIES,
			STORE_ROAD_ADDRESS_NAME,
			STORE_PHOTO_URLS,
			STORE_KAKAO_PLACE_URL,
			STORE_PHONE_NUMBER
		);
	}

}
