package com.prgrms.mukvengers.utils;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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

	public static List<CrewMember> createCrewMembers(Long userId, Crew crew, CrewMemberRole role, Integer capacity) {

		return IntStream.range(1, capacity)
			.mapToObj(i -> createCrewMember(userId, crew, role)).collect(Collectors.toList());
	}
}