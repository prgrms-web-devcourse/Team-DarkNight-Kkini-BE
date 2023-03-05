package com.prgrms.mukvengers.domain.crew.service;

import org.springframework.data.domain.Pageable;

import com.prgrms.mukvengers.domain.crew.dto.request.CreateCrewRequest;
import com.prgrms.mukvengers.domain.crew.dto.request.SearchCrewRequest;
import com.prgrms.mukvengers.domain.crew.dto.request.UpdateStatusRequest;
import com.prgrms.mukvengers.domain.crew.dto.response.CrewPageResponse;
import com.prgrms.mukvengers.domain.crew.dto.response.CrewResponse;
import com.prgrms.mukvengers.domain.crew.dto.response.CrewResponses;
import com.prgrms.mukvengers.domain.crew.dto.response.MyCrewResponse;
import com.prgrms.mukvengers.global.common.dto.IdResponse;

public interface CrewService {

	IdResponse create(CreateCrewRequest createCrewRequest, Long userId);

	MyCrewResponse getByUserId(Long userId);

	CrewResponse getById(Long crewId);

	CrewPageResponse getByMapStoreId(String mapStoreId, Pageable pageable);

	CrewResponses getByLocation(SearchCrewRequest distanceRequest);

	void updateStatus(UpdateStatusRequest updateStatusRequest);

}
