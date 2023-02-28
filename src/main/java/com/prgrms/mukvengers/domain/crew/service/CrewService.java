package com.prgrms.mukvengers.domain.crew.service;

import org.springframework.data.domain.Pageable;

import com.prgrms.mukvengers.domain.crew.dto.request.CreateCrewRequest;
import com.prgrms.mukvengers.domain.crew.dto.request.UpdateStatusRequest;
import com.prgrms.mukvengers.domain.crew.dto.response.CrewPageResponse;
import com.prgrms.mukvengers.domain.crew.dto.response.CrewResponses;
import com.prgrms.mukvengers.global.common.dto.IdResponse;

public interface CrewService {

	IdResponse create(CreateCrewRequest createCrewRequest, Long userId);

	CrewPageResponse getByMapStoreId(String mapStoreId, Pageable pageable);

	CrewResponses getByLocation(String latitude, String longitude);

	void updateStatus(UpdateStatusRequest updateStatusRequest);

}