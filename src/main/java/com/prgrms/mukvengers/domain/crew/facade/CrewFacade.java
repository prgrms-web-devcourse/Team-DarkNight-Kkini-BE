package com.prgrms.mukvengers.domain.crew.facade;

import com.prgrms.mukvengers.domain.crew.dto.request.CreateCrewRequest;
import com.prgrms.mukvengers.domain.crew.service.CrewService;
import com.prgrms.mukvengers.domain.store.service.StoreService;
import com.prgrms.mukvengers.global.base.dto.IdResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CrewFacade {

    private final StoreService storeService;
    private final CrewService crewService;

    public IdResponse create(CreateCrewRequest createCrewRequest, Long userId) {
        Long storeId = storeService.findByPlaceId(createCrewRequest.createStoreRequest());
        Long crewId = crewService.create(createCrewRequest, userId, storeId);
        return new IdResponse(crewId);
    }


}
