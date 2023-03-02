package com.prgrms.mukvengers.domain.crew.mapper;

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

@Mapper(componentModel = "spring")
public interface CrewMapper {

	@Mapping(target = "status", source = "createCrewRequest.status", qualifiedByName = "statusMethod")
	@Mapping(target = "category", source = "createCrewRequest.category", qualifiedByName = "categoryMethod")
	@Mapping(target = "promiseTime", source = "createCrewRequest.promiseTime")
	@Mapping(target = "store", source = "store")
	Crew toCrew(CreateCrewRequest createCrewRequest, Store store);

	@Mapping(target = "latitude", source = "crew", qualifiedByName = "latitudeMethod")
	@Mapping(target = "longitude", source = "crew", qualifiedByName = "longitudeMethod")
	@Mapping(target = "store", source = "crew.store", qualifiedByName = "storeMethod")
	@Mapping(target = "promiseTime", source = "crew.promiseTime")
	CrewResponse toCrewResponse(Crew crew);

	@Named("storeMethod")
	default StoreResponse mapStore(Store store) {
		return new StoreResponse(store.getId(), String.valueOf(store.getLocation().getX()),
			String.valueOf(store.getLocation().getY()), store.getMapStoreId());

	}

	@Named("latitudeMethod")
	default String mapLatitude(Crew crew) {
		return String.valueOf(crew.getLocation().getY());
	}

	@Named("longitudeMethod")
	default String mapLongitude(Crew crew) {
		return String.valueOf(crew.getLocation().getX());
	}

	@Named("statusMethod")
	default Status mapStatus(String status) {
		return Status.getStatus(status);
	}

	@Named("categoryMethod")
	default Category mapCategory(String category) {
		return Category.getCategory(category);
	}

}


