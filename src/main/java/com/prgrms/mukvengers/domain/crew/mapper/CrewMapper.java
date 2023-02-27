package com.prgrms.mukvengers.domain.crew.mapper;

import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.prgrms.mukvengers.domain.crew.dto.request.CreateCrewRequest;
import com.prgrms.mukvengers.domain.crew.dto.response.CrewResponse;
import com.prgrms.mukvengers.domain.crew.model.Crew;
import com.prgrms.mukvengers.domain.crew.model.vo.Category;
import com.prgrms.mukvengers.domain.crew.model.vo.Status;
import com.prgrms.mukvengers.domain.store.dto.response.StoreResponse;
import com.prgrms.mukvengers.domain.store.model.Store;
import com.prgrms.mukvengers.domain.user.model.User;

@Mapper(componentModel = "spring")
public interface CrewMapper {

	@Mapping(target = "leader", source = "user")
	@Mapping(target = "status", source = "createCrewRequest.status", qualifiedByName = "statusMethod")
	@Mapping(target = "category", source = "createCrewRequest.category", qualifiedByName = "categoryMethod")
	@Mapping(target = "location", source = "createCrewRequest", qualifiedByName = "pointMethod")
	Crew toCrew(CreateCrewRequest createCrewRequest, User user, Store store);

	@Mapping(target = "latitude", source = "crew", qualifiedByName = "latitudeMethod")
	@Mapping(target = "longitude", source = "crew", qualifiedByName = "longitudeMethod")
	@Mapping(target = "store", source = "crew.store", qualifiedByName = "storeMethod")
	CrewResponse toCrewResponse(Crew crew);

	@Named("storeMethod")
	default StoreResponse mapStore(Store store) {
		return new StoreResponse(store.getId(), String.valueOf(store.getLocation().getX()),
			String.valueOf(store.getLocation().getY()), store.getMapStoreId());

	}

	@Named("latitudeMethod")
	default String mapLatitude(Crew crew) {
		return String.valueOf(crew.getLocation().getX());
	}

	@Named("longitudeMethod")
	default String mapLongitude(Crew crew) {
		return String.valueOf(crew.getLocation().getY());
	}

	@Named("statusMethod")
	default Status mapStatus(String status) {
		return Status.getStatus(status);
	}

	@Named("categoryMethod")
	default Category mapCategory(String category) {
		return Category.getCategory(category);
	}

	@Named("pointMethod")
	default Point mapPoint(CreateCrewRequest createCrewRequest) {

		String pointWKT = String.format("POINT(%s %s)", createCrewRequest.latitude(),
			createCrewRequest.longitude());

		try {
			return (Point)new WKTReader().read(pointWKT);
		} catch (ParseException e) {
			throw new IllegalArgumentException();
		}
	}
}


