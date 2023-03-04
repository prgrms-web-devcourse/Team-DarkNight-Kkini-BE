package com.prgrms.mukvengers.utils;

import com.prgrms.mukvengers.domain.crew.model.Crew;
import com.prgrms.mukvengers.domain.crewmember.model.CrewMember;
import com.prgrms.mukvengers.domain.crewmember.model.vo.Role;

public class CrewMemberObjectProvider {

	public static CrewMember createCrewMember(Long userId, Crew crew, Role role) {

		return CrewMember.builder()
			.userId(userId)
			.crew(crew)
			.role(role)
			.build();
	}
}