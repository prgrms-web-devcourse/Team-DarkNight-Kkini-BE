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
	private final static String STORE_MAP_STORE_ID = "16618597";

	public static Store createStore() {

		Point location = STORE_GF.createPoint(new Coordinate(STORE_LONGITUDE, STORE_LATITUDE));

		return Store.builder()
			.location(location)
			.mapStoreId(STORE_MAP_STORE_ID)
			.build();

	}

	public static void createStore(Point location) {

		Store.builder()
			.location(location)
			.mapStoreId(STORE_MAP_STORE_ID)
			.build();

	}

	public static void createStore(double longitude, double latitude) {

		Point location = STORE_GF.createPoint(new Coordinate(longitude, latitude));

		Store.builder()
			.location(location)
			.mapStoreId(STORE_MAP_STORE_ID)
			.build();

	}

	public static Store createStore(String mapStoreId) {

		Point location = STORE_GF.createPoint(new Coordinate(STORE_LONGITUDE, STORE_LATITUDE));

		return Store.builder()
			.location(location)
			.mapStoreId(mapStoreId)
			.build();

	}

	public static CreateStoreRequest getCreateStoreRequest(String mapStoreId) {
		return new CreateStoreRequest(String.valueOf(STORE_LONGITUDE), String.valueOf(STORE_LATITUDE), mapStoreId);
	}

}
