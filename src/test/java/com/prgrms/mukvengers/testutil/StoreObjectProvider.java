package com.prgrms.mukvengers.testutil;

import org.locationtech.jts.geom.Point;

import com.prgrms.mukvengers.domain.store.model.Store;

public class StoreObjectProvider {

	public static Store createStore(Point location, String apiId) {

		return Store.builder()
			.location(location)
			.apiId(apiId)
			.build();

	}

}
