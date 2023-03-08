package com.prgrms.mukvengers.utils;

import com.prgrms.mukvengers.domain.crew.model.Crew;
import com.prgrms.mukvengers.domain.crewmember.model.CrewMember;
import com.prgrms.mukvengers.domain.crewmember.model.vo.CrewMemberRole;

public class CrewMemberObjectProvider {

	public static CrewMember createCrewMember(Long userId, Crew crew, CrewMemberRole crewMemberRole) {

		return CrewMember.builder()
			.userId(userId)
			.crew(crew)
			.crewMemberRole(crewMemberRole)
			.build();
	}
}