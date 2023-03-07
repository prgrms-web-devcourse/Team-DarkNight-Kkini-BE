package com.prgrms.mukvengers.domain.crewmember.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.prgrms.mukvengers.domain.crew.model.Crew;
import com.prgrms.mukvengers.domain.crewmember.dto.response.CrewMemberResponse;
import com.prgrms.mukvengers.domain.crewmember.model.CrewMember;
import com.prgrms.mukvengers.domain.crewmember.model.vo.Role;
import com.prgrms.mukvengers.domain.user.model.User;

@Mapper(componentModel = "spring")
public interface CrewMemberMapper {

	CrewMember toCrewMember(Crew crew, Long userId, Role role);

	@Mapping(target = "userId", source = "user.id")
	@Mapping(target = "nickname", source = "user.nickname")
	@Mapping(target = "profileImgUrl", source = "user.profileImgUrl")
	@Mapping(target = "role", source = "role")
	CrewMemberResponse toCrewMemberResponse(User user, Role role);
}
