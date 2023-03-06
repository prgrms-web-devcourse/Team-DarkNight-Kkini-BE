package com.prgrms.mukvengers.domain.crew.facade;

import static com.prgrms.mukvengers.domain.crewmember.model.vo.Role.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prgrms.mukvengers.domain.crew.dto.request.CreateCrewRequest;
import com.prgrms.mukvengers.domain.crew.service.CrewService;
import com.prgrms.mukvengers.domain.crewmember.service.CrewMemberService;
import com.prgrms.mukvengers.global.common.dto.IdResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CrewFacadeService {

	private final CrewService crewService;
	private final CrewMemberService crewMemberService;

	@Transactional
	public IdResponse create(CreateCrewRequest createCrewRequest, Long userId) {

		IdResponse idResponse = crewService.create(createCrewRequest, userId);

		crewMemberService.create(idResponse.id(), userId, LEADER);

		return idResponse;
	}

}
