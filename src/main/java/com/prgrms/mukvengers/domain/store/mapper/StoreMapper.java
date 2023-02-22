package com.prgrms.mukvengers.domain.store.mapper;

import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.prgrms.mukvengers.domain.store.dto.request.CreateStoreRequest;
import com.prgrms.mukvengers.domain.store.model.Store;

@Mapper(componentModel = "spring")
public interface StoreMapper {

	@Mapping(target = "location", source = "createStoreRequest", qualifiedByName = "pointMethod")
	Store toStore(CreateStoreRequest createStoreRequest);

	@Named("pointMethod")
	default Point mapPoint(CreateStoreRequest createStoreRequest) {

		GeometryFactory gf = new GeometryFactory();

		String pointWKT = String.format("POINT(%s %s)", createStoreRequest.latitude(),
			createStoreRequest.longitude());

		try {
			return (Point)new WKTReader().read(pointWKT);
		} catch (ParseException e) {
			throw new IllegalArgumentException();
		}
	}
}
