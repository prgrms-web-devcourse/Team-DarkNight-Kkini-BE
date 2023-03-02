package com.prgrms.mukvengers.utils;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;

import com.prgrms.mukvengers.domain.store.dto.request.CreateStoreRequest;
import com.prgrms.mukvengers.domain.store.model.Store;

public class StoreObjectProvider {

	private final static GeometryFactory gf = new GeometryFactory();
	private final static double longitude = -147.4654321321;
	private final static double latitude = 35.75413579;
	private final static String mapStoreId = "16618597";

	public static Store createStore() {

		Point location = gf.createPoint(new Coordinate(longitude, latitude));

		return Store.builder()
			.location(location)
			.mapStoreId(mapStoreId)
			.build();

	}

	public static void createStore(Point location) {

		Store.builder()
			.location(location)
			.mapStoreId(mapStoreId)
			.build();

	}

	public static void createStore(double longitude, double latitude) {

		Point location = gf.createPoint(new Coordinate(longitude, latitude));

		Store.builder()
			.location(location)
			.mapStoreId(mapStoreId)
			.build();

	}

	public static Store createStore(String mapStoreId) {

		Point location = gf.createPoint(new Coordinate(longitude, latitude));

		return Store.builder()
			.location(location)
			.mapStoreId(mapStoreId)
			.build();

	}

	public static CreateStoreRequest getCreateStoreRequest(String mapStoreId) {
		return new CreateStoreRequest(String.valueOf(longitude), String.valueOf(latitude), mapStoreId);
	}

}
