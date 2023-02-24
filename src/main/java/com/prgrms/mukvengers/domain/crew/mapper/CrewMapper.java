package com.prgrms.mukvengers.domain.crew.mapper;

import org.mapstruct.Mapper;

import com.prgrms.mukvengers.domain.crew.dto.CreateCrewRequest;
import com.prgrms.mukvengers.domain.crew.model.Crew;
import com.prgrms.mukvengers.domain.store.model.Store;
import com.prgrms.mukvengers.domain.user.model.User;

@Mapper(componentModel = "spring")
public interface CrewMapper {

	Crew toCrew(CreateCrewRequest createCrewRequest, User user, Store store);

}
