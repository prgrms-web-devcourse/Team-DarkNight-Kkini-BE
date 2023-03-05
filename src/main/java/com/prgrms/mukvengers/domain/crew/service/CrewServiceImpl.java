package com.prgrms.mukvengers.domain.crew.service;

import java.util.List;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prgrms.mukvengers.domain.crew.dto.request.CreateCrewRequest;
import com.prgrms.mukvengers.domain.crew.dto.request.SearchCrewRequest;
import com.prgrms.mukvengers.domain.crew.dto.request.UpdateStatusRequest;
import com.prgrms.mukvengers.domain.crew.dto.response.CrewPageResponse;
import com.prgrms.mukvengers.domain.crew.dto.response.CrewResponse;
import com.prgrms.mukvengers.domain.crew.dto.response.CrewResponses;
import com.prgrms.mukvengers.domain.crew.exception.CrewNotFoundException;
import com.prgrms.mukvengers.domain.crew.mapper.CrewMapper;
import com.prgrms.mukvengers.domain.crew.model.Crew;
import com.prgrms.mukvengers.domain.crew.repository.CrewRepository;
import com.prgrms.mukvengers.domain.store.exception.StoreNotFoundException;
import com.prgrms.mukvengers.domain.store.model.Store;
import com.prgrms.mukvengers.domain.store.repository.StoreRepository;
import com.prgrms.mukvengers.domain.user.exception.UserNotFoundException;
import com.prgrms.mukvengers.domain.user.repository.UserRepository;
import com.prgrms.mukvengers.global.common.dto.IdResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CrewServiceImpl implements CrewService {

	private final CrewRepository crewRepository;
	private final UserRepository userRepository;
	private final StoreRepository storeRepository;
	private final CrewMapper crewMapper;

	@Override
	@Transactional
	public IdResponse create(CreateCrewRequest createCrewRequest, Long userId) {

		userRepository.findById(userId)
			.orElseThrow(() -> new UserNotFoundException(userId));

		Store store = storeRepository.findByMapStoreId(createCrewRequest.mapStoreId())
			.orElseThrow(() -> new StoreNotFoundException(createCrewRequest.mapStoreId()));

		Crew crew = crewMapper.toCrew(createCrewRequest, store);

		crewRepository.save(crew);

		return new IdResponse(crew.getId());
	}

	@Override
	public CrewResponse getById(Long crewId) {

		Crew crew = crewRepository.findById(crewId)
			.orElseThrow(() -> new CrewNotFoundException(crewId));

		return crewMapper.toCrewResponse(crew);
	}

	@Override
	public CrewPageResponse getByMapStoreId(String mapStoreId, Pageable pageable) {

		Page<CrewResponse> responses = crewRepository.findAllByMapStoreId(mapStoreId, pageable)
			.map(crewMapper::toCrewResponse);

		return new CrewPageResponse(responses);
	}

	@Override
	public CrewResponses getByLocation(SearchCrewRequest distanceRequest) {

		GeometryFactory gf = new GeometryFactory();

		Point location = gf.createPoint(new Coordinate(distanceRequest.longitude(), distanceRequest.latitude()));

		List<CrewResponse> responses = crewRepository.findAllByLocation(location, distanceRequest.distance())
			.stream().map(crewMapper::toCrewResponse).toList();

		return new CrewResponses(responses);
	}

	@Override
	public void updateStatus(UpdateStatusRequest updateStatusRequest) {

		Crew crew = crewRepository.findById(updateStatusRequest.crewId())
			.orElseThrow(() -> new CrewNotFoundException(updateStatusRequest.crewId()));

		crew.changeStatus(updateStatusRequest.status());

	}

}
