package com.prgrms.mukvengers.domain.crewmember.mapper;

import org.mapstruct.Mapper;

import com.prgrms.mukvengers.domain.crew.model.Crew;
import com.prgrms.mukvengers.domain.crewmember.model.CrewMember;
import com.prgrms.mukvengers.domain.crewmember.model.vo.Role;

@Mapper(componentModel = "spring")
public interface CrewMemberMapper {

	CrewMember toCrewMember(Crew crew, Long userId, Role role);
}
