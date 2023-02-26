package com.prgrms.mukvengers.domain.crew.service;

import java.util.List;

import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prgrms.mukvengers.domain.crew.dto.request.CreateCrewRequest;
import com.prgrms.mukvengers.domain.crew.dto.request.UpdateStatusRequest;
import com.prgrms.mukvengers.domain.crew.dto.response.CrewResponse;
import com.prgrms.mukvengers.domain.crew.dto.response.CrewResponses;
import com.prgrms.mukvengers.domain.crew.dto.response.CrewSliceResponse;
import com.prgrms.mukvengers.domain.crew.exception.CrewNotFoundException;
import com.prgrms.mukvengers.domain.crew.mapper.CrewMapper;
import com.prgrms.mukvengers.domain.crew.model.Crew;
import com.prgrms.mukvengers.domain.crew.repository.CrewRepository;
import com.prgrms.mukvengers.domain.store.exception.StoreNotFoundException;
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
public class CrewServiceImpl implements CrewService {

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
			.orElseThrow(() -> new StoreNotFoundException(createCrewRequest.mapStoreId()));

		Crew crew = crewMapper.toCrew(createCrewRequest, user, store);

		crewRepository.save(crew);

		return new IdResponse(crew.getId());
	}

	@Override
	public CrewSliceResponse getByMapStoreId(String mapStoreId, Long cursorId, Integer size) {
		Pageable pageable = PageRequest.of(0, size, Sort.by("id").descending());

		Slice<CrewResponse> responses;

		if (cursorId == null) {
			responses = crewRepository.joinStoreByMapStoreIdFirst(mapStoreId, pageable)
				.map(crewMapper::toCrewResponse);
		} else {
			responses = crewRepository.joinStoreByMapStoreId(mapStoreId, cursorId, pageable)
				.map(crewMapper::toCrewResponse);
		}

		return new CrewSliceResponse(responses);
	}

	@Override
	public CrewResponses getByLocation(String latitude, String longitude) {
		String pointWKT = String.format("POINT(%s %s)", latitude,
			longitude);

		try {
			Point point = (Point)new WKTReader().read(pointWKT);

			List<CrewResponse> responses = crewRepository.findAllByLocation(point, 500)
				.stream().map(crewMapper::toCrewResponse).toList();

			return new CrewResponses(responses);
		} catch (ParseException e) {
			throw new IllegalArgumentException();
		}
	}

	@Override
	public void updateStatus(UpdateStatusRequest updateStatusRequest) {
		Crew crew = crewRepository.findById(updateStatusRequest.crewId())
			.orElseThrow(() -> new CrewNotFoundException(updateStatusRequest.crewId()));

		crew.changeStatus(updateStatusRequest.status());

	}

}
