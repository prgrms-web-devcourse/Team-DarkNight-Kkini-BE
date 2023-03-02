package com.prgrms.mukvengers.domain.store.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.locationtech.jts.geom.Point;

import com.prgrms.mukvengers.utils.StoreObjectProvider;

class StoreTest {

	@Test
	@DisplayName("[성공] 모든 필드가 유효하면 인스턴스 생성한다.")
	void constructor_success() {

		assertDoesNotThrow(() -> StoreObjectProvider.createStore());
	}

	@ParameterizedTest
	@NullSource
	@DisplayName("[실패] Point값이 null일 경우 인스턴스 생성에 실패한다.")
	void validatePosition_fail(Point location) {

		assertThrows(IllegalArgumentException.class, () -> StoreObjectProvider.createStore(location));
	}

	@ParameterizedTest
	@ValueSource(doubles = {-191.45473, 195.245678, 242.7564, -242.3456})
	@DisplayName("[실패] 경도값이 유효하지 않으면 인스턴스 생성에 실패한다.")
	void validateLongitude_fail(double longitude) {

		double latitude = 50;

		assertThrows(IllegalArgumentException.class, () -> StoreObjectProvider.createStore(longitude, latitude));
	}

	@ParameterizedTest
	@ValueSource(doubles = {-91.45473, 95.245678, 102.7564, -142.3456})
	@DisplayName("[실패] 위도값이 유효하지 않으면 인스턴스 생성에 실패한다.")
	void validateLatitude_fail(double latitude) {

		double longitude = 50;

		assertThrows(IllegalArgumentException.class, () -> StoreObjectProvider.createStore(longitude, latitude));
	}

	@ParameterizedTest
	@NullAndEmptySource
	@DisplayName("[실패] apiId값이 null이거나, 빈 문자열일 경우 인스턴스 생성에 실패한다.")
	void validateApiId_fail(String mapStoreId) {

		assertThrows(IllegalArgumentException.class, () -> StoreObjectProvider.createStore(mapStoreId));
	}

}