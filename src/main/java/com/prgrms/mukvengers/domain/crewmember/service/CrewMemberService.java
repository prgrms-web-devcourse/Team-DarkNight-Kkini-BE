package com.prgrms.mukvengers.domain.crewmember.service;

import com.prgrms.mukvengers.domain.crewmember.model.vo.CrewMemberRole;
import com.prgrms.mukvengers.global.common.dto.IdResponse;

public interface CrewMemberService {

	IdResponse create(Long crewId, Long userId, CrewMemberRole crewMemberRole);
}
