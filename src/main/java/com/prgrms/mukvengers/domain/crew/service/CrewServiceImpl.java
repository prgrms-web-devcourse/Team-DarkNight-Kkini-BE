package com.prgrms.mukvengers.domain.crew.service;

import java.util.List;

import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prgrms.mukvengers.domain.crew.dto.request.CreateCrewRequest;
import com.prgrms.mukvengers.domain.crew.dto.response.CrewResponse;
import com.prgrms.mukvengers.domain.crew.dto.response.CrewResponses;
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
public class CrewServiceImpl implements CrewService{

	private final CrewRepository crewRepository;
	private final UserRepository userRepository;
	private final StoreRepository storeRepository;
	private final CrewMapper crewMapper;

	@Override
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

	@Override
	public CrewResponses findByMapStoreId(String mapStoreId) {
		List<CrewResponse> responses = crewRepository.joinStoreByMapStoreId(mapStoreId)
			.stream().map(crewMapper::toCrewResponse).toList();

		return new CrewResponses(responses);

	}

	@Override
	public CrewResponses findByLocation(String latitude, String longitude) {
		String pointWKT = String.format("POINT(%s %s)", latitude,
			longitude);

		try {
			Point point = (Point)new WKTReader().read(pointWKT);

			List<CrewResponse> responses = crewRepository.findAllByDistance(point, 500)
				.stream().map(crewMapper::toCrewResponse).toList();

			return new CrewResponses(responses);
		} catch (ParseException e) {
			throw new IllegalArgumentException();
		}
	}

}
