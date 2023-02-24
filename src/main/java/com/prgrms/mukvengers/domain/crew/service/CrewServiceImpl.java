package com.prgrms.mukvengers.domain.crew.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prgrms.mukvengers.domain.crew.dto.CreateCrewRequest;
import com.prgrms.mukvengers.domain.crew.mapper.CrewMapper;
import com.prgrms.mukvengers.domain.crew.model.Crew;
import com.prgrms.mukvengers.domain.crew.repository.CrewRepository;
import com.prgrms.mukvengers.domain.store.model.Store;
import com.prgrms.mukvengers.domain.store.repository.StoreRepository;
import com.prgrms.mukvengers.domain.user.exception.UserNotFoundException;
import com.prgrms.mukvengers.domain.user.model.User;
import com.prgrms.mukvengers.domain.user.repository.UserRepository;
import com.prgrms.mukvengers.global.common.dto.IdResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CrewServiceImpl {

	private final CrewRepository crewRepository;
	private final UserRepository userRepository;
	private final StoreRepository storeRepository;
	private final CrewMapper crewMapper;

	@Transactional
	public IdResponse create(CreateCrewRequest createCrewRequest, Long userId) {
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new UserNotFoundException(userId));

		Store store = storeRepository.findByMapStoreId(createCrewRequest.mapStoreId())
			.orElseThrow();

		Crew crew = crewMapper.toCrew(createCrewRequest, user, store);

		crewRepository.save(crew);

		return new IdResponse(crew.getId());
	}

}
