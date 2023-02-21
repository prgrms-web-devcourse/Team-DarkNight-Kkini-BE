package com.prgrms.mukvengers.domain.store.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;

import com.prgrms.mukvengers.testutil.StoreObjectProvider;

class StoreTest {

	private final GeometryFactory gf = new GeometryFactory();

	private final double longitude = -147.4654321321;

	private final double latitude = 35.75413579;

	private final String apiId = "16618597";

	@Test
	@DisplayName("모든 필드가 유효하면 인스턴스 생성한다.")
	void constructor_success() {

		Point location = gf.createPoint(new Coordinate(longitude, latitude));

		assertDoesNotThrow(() -> StoreObjectProvider.createStore(location, apiId));

	}

	@ParameterizedTest
	@NullSource
	@DisplayName("Point값이 null일 경우 인스턴스 생성에 실패한다.")
	void validatePosition(Point location) {

		assertThrows(IllegalArgumentException.class, () -> StoreObjectProvider.createStore(location, apiId));
	}

	@ParameterizedTest
	@ValueSource(doubles = {-191.45473, 195.245678, 242.7564, -242.3456})
	@DisplayName("경도값이 유효하지 않으면 인스턴스 생성에 실패한다.")
	void validateLongitude(double longitude) {

		Point location = gf.createPoint(new Coordinate(longitude, latitude));

		assertThrows(IllegalArgumentException.class, () -> StoreObjectProvider.createStore(location, apiId));
	}

	@ParameterizedTest
	@ValueSource(doubles = {-91.45473, 95.245678, 102.7564, -142.3456})
	@DisplayName("위도값이 유효하지 않으면 인스턴스 생성에 실패한다.")
	void validateLatitude(double latitude) {

		Point location = gf.createPoint(new Coordinate(longitude, latitude));

		assertThrows(IllegalArgumentException.class, () -> StoreObjectProvider.createStore(location, apiId));
	}

	@ParameterizedTest
	@NullAndEmptySource
	@DisplayName("apiId값이 null이거나, 빈 문자열일 경우 인스턴스 생성에 실패한다.")
	void validateApiId(String apiId) {

		Point location = gf.createPoint(new Coordinate(longitude, latitude));

		assertThrows(IllegalArgumentException.class, () -> StoreObjectProvider.createStore(location, apiId));
	}

}