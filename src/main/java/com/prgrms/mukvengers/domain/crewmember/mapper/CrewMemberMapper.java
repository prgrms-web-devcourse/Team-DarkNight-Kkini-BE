package com.prgrms.mukvengers.domain.crewmember.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.prgrms.mukvengers.domain.crew.model.Crew;
import com.prgrms.mukvengers.domain.crewmember.dto.response.CrewMemberResponse;
import com.prgrms.mukvengers.domain.crewmember.model.CrewMember;
import com.prgrms.mukvengers.domain.crewmember.model.vo.CrewMemberRole;
import com.prgrms.mukvengers.domain.user.model.User;

@Mapper(componentModel = "spring")
public interface CrewMemberMapper {

	CrewMember toCrewMember(Crew crew, Long userId, CrewMemberRole crewMemberRole);

	@Mapping(target = "userId", source = "user.id")
	@Mapping(target = "nickname", source = "user.nickname")
	@Mapping(target = "profileImgUrl", source = "user.profileImgUrl")
	@Mapping(target = "crewMemberRole", source = "crewMemberRole")
	CrewMemberResponse toCrewMemberResponse(User user, CrewMemberRole crewMemberRole);
}
