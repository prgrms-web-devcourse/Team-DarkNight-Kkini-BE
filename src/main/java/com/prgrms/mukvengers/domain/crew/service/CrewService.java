package com.prgrms.mukvengers.domain.crew.service;

import org.springframework.transaction.annotation.Transactional;

import com.prgrms.mukvengers.domain.crew.dto.request.CreateCrewRequest;
import com.prgrms.mukvengers.domain.crew.dto.response.CrewResponses;
import com.prgrms.mukvengers.global.common.dto.IdResponse;

public interface CrewService {

	IdResponse create(CreateCrewRequest createCrewRequest, Long userId);

	CrewResponses findByMapStoreId(String mapStoreId);
}
