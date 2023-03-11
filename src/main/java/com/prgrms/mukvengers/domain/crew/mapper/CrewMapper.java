package com.prgrms.mukvengers.domain.crew.mapper;

import static org.mapstruct.ReportingPolicy.*;

import java.util.List;

import org.locationtech.jts.geom.Point;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.prgrms.mukvengers.domain.crew.dto.request.CreateCrewRequest;
import com.prgrms.mukvengers.domain.crew.dto.response.CrewDetailResponse;
import com.prgrms.mukvengers.domain.crew.dto.response.CrewLocationResponse;
import com.prgrms.mukvengers.domain.crew.dto.response.CrewResponse;
import com.prgrms.mukvengers.domain.crew.model.Crew;
import com.prgrms.mukvengers.domain.crewmember.dto.response.CrewMemberResponse;
import com.prgrms.mukvengers.domain.proposal.model.vo.ProposalStatus;
import com.prgrms.mukvengers.domain.store.dto.response.StoreResponse;
import com.prgrms.mukvengers.domain.store.model.Store;

@Mapper(componentModel = "spring", unmappedSourcePolicy = IGNORE)
public interface CrewMapper {

	@Mapping(target = "category", source = "createCrewRequest.category")
	@Mapping(target = "promiseTime", source = "createCrewRequest.promiseTime")
	@Mapping(target = "store", source = "store")
	@Mapping(target = "location", source = "store.location")
	Crew toCrew(CreateCrewRequest createCrewRequest, Store store);

	@Mapping(target = "promiseTime", source = "crew.promiseTime")
	CrewResponse toCrewResponse(Crew crew, Integer currentMember);

	@Mapping(target = "response", source = "storeResponse")
	@Mapping(target = "promiseTime", source = "crew.promiseTime")
	@Mapping(target = "id", source = "crew.id")
	@Mapping(target = "crewStatus", source = "crew.status")
	@Mapping(target = "proposalStatus", source = "proposalStatus")
	CrewDetailResponse toCrewDetailResponse(Crew crew, Integer currentMember,
		List<CrewMemberResponse> members, StoreResponse storeResponse, ProposalStatus proposalStatus);

	@Mapping(target = "latitude", source = "location", qualifiedByName = "latitudeMethod")
	@Mapping(target = "longitude", source = "location", qualifiedByName = "longitudeMethod")
	CrewLocationResponse toCrewLocationResponse(Point location, Long storeId, String placeName);

	@Named("latitudeMethod")
	default Double mapLatitude(Point location) {
		return location.getY();
	}

	@Named("longitudeMethod")
	default Double mapLongitude(Point location) {
		return location.getX();
	}
}


