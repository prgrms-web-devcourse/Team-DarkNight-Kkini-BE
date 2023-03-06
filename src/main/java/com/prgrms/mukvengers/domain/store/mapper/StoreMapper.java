package com.prgrms.mukvengers.domain.store.mapper;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.prgrms.mukvengers.domain.store.dto.request.CreateStoreRequest;
import com.prgrms.mukvengers.domain.store.dto.response.StoreResponse;
import com.prgrms.mukvengers.domain.store.model.Store;

@Mapper(componentModel = "spring")
public interface StoreMapper {

	@Mapping(target = "location", source = "createStoreRequest", qualifiedByName = "pointMethod")
	Store toStore(CreateStoreRequest createStoreRequest);

	@Mapping(target = "longitude", source = "store", qualifiedByName = "longitudeMethod")
	@Mapping(target = "latitude", source = "store", qualifiedByName = "latitudeMethod")
	StoreResponse toStoreResponse(Store store);

	@Named("longitudeMethod")
	default Double mapLongitude(Store store) {
		return store.getLocation().getX();
	}

	@Named("latitudeMethod")
	default Double mapLatitude(Store store) {
		return store.getLocation().getY();
	}

	@Named("pointMethod")
	default Point mapPoint(CreateStoreRequest request) {

		GeometryFactory gf = new GeometryFactory();

		return gf.createPoint(new Coordinate(
			request.longitude(),
			request.latitude()));

	}

}
