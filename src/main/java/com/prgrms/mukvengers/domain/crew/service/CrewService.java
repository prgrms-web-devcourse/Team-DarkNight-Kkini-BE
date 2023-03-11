package com.prgrms.mukvengers.domain.crew.service;

import org.springframework.data.domain.Pageable;

import com.prgrms.mukvengers.domain.crew.dto.request.CreateCrewRequest;
import com.prgrms.mukvengers.domain.crew.dto.request.SearchCrewRequest;
import com.prgrms.mukvengers.domain.crew.dto.response.CrewDetailResponse;
import com.prgrms.mukvengers.domain.crew.dto.response.CrewLocationResponses;
import com.prgrms.mukvengers.domain.crew.dto.response.CrewPageResponse;
import com.prgrms.mukvengers.domain.crew.dto.response.CrewResponses;
import com.prgrms.mukvengers.domain.crew.dto.response.CrewStatusResponse;
import com.prgrms.mukvengers.domain.crew.model.vo.CrewStatus;
import com.prgrms.mukvengers.global.common.dto.IdResponse;

public interface CrewService {

	IdResponse create(CreateCrewRequest createCrewRequest, Long userId);

	CrewResponses getByUserId(Long userId);

	CrewDetailResponse getById(Long userId, Long crewId);

	CrewPageResponse getByPlaceId(Long userId, String mapStoreId, Pageable pageable);

	CrewLocationResponses getByLocation(SearchCrewRequest distanceRequest);

	CrewStatusResponse updateStatus(Long crewId, Long userId, CrewStatus crewStatus);
}
