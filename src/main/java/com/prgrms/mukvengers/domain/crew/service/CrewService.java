package com.prgrms.mukvengers.domain.crew.service;

import com.prgrms.mukvengers.domain.crew.dto.request.CreateCrewRequest;
import com.prgrms.mukvengers.domain.crew.dto.request.UpdateStatusRequest;
import com.prgrms.mukvengers.domain.crew.dto.response.CrewResponses;
import com.prgrms.mukvengers.domain.crew.dto.response.CrewSliceResponse;
import com.prgrms.mukvengers.global.common.dto.IdResponse;

public interface CrewService {

	IdResponse create(CreateCrewRequest createCrewRequest, Long userId);

	CrewSliceResponse getByMapStoreId(String mapStoreId, Long cursorId, Integer size);

	CrewResponses getByLocation(String latitude, String longitude);

	void updateStatus(UpdateStatusRequest updateStatusRequest);

}
