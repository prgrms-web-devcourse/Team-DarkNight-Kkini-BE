package com.prgrms.mukvengers.domain.crew.service;

import com.prgrms.mukvengers.domain.crew.dto.CreateCrewRequest;
import com.prgrms.mukvengers.global.common.dto.IdResponse;

public interface CrewService {

	IdResponse create(CreateCrewRequest createCrewRequest, Long userId);
}
