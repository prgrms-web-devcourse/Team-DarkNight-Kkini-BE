package com.prgrms.mukvengers.domain.crew.handler;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.prgrms.mukvengers.domain.crew.dto.event.CreateCrewEvent;
import com.prgrms.mukvengers.domain.crewmember.model.vo.CrewMemberRole;
import com.prgrms.mukvengers.domain.crewmember.service.CrewMemberService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CrewEventHandler {

	private final CrewMemberService crewMemberService;

	@EventListener
	public void create(CreateCrewEvent event) {
		crewMemberService.create(event.crewId(), event.userId(), CrewMemberRole.LEADER);
	}
}
