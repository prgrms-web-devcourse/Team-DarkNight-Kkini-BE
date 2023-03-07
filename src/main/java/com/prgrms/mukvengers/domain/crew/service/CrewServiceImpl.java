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
import com.prgrms.mukvengers.domain.crew.dto.response.MyCrewResponse;
import com.prgrms.mukvengers.domain.crew.exception.CrewNotFoundException;
import com.prgrms.mukvengers.domain.crew.mapper.CrewMapper;
import com.prgrms.mukvengers.domain.crew.model.Crew;
import com.prgrms.mukvengers.domain.crew.repository.CrewRepository;
import com.prgrms.mukvengers.domain.crewmember.repository.CrewMemberRepository;
import com.prgrms.mukvengers.domain.store.mapper.StoreMapper;
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
	private final CrewMemberRepository crewMemberRepository;
	private final CrewMapper crewMapper;
	private final StoreMapper storeMapper;

	@Override
	@Transactional
	public IdResponse create(CreateCrewRequest createCrewRequest, Long userId) {

		userRepository.findById(userId)
			.orElseThrow(() -> new UserNotFoundException(userId));

		Store store = storeRepository.findByPlaceId(createCrewRequest.createStoreRequest().placeId())
			.orElse(storeRepository.save(storeMapper.toStore(createCrewRequest.createStoreRequest())));

		Crew crew = crewMapper.toCrew(createCrewRequest, store);

		crewRepository.save(crew);

		return new IdResponse(crew.getId());
	}

	@Override
	public MyCrewResponse getByUserId(Long userId) {

		List<CrewResponse> responses = crewMemberRepository.findAllByUserIdOrderByStatus(userId)
			.stream()
			.map(crew -> crewMapper.toCrewResponse(
				crew,
				crewMemberRepository.countCrewMemberByCrewId(crew.getId())))
			.toList();

		User user = userRepository.findById(userId)
			.orElseThrow(() -> new UserNotFoundException(userId));

		return new MyCrewResponse(responses, user.getProfileImgUrl());
	}

	@Override
	public CrewResponse getById(Long crewId) {

		Crew crew = crewRepository.findById(crewId)
			.orElseThrow(() -> new CrewNotFoundException(crewId));

		Integer currentMember = crewMemberRepository.countCrewMemberByCrewId(crewId);

		return crewMapper.toCrewResponse(crew, currentMember);
	}

	@Override
	public CrewPageResponse getByPlaceId(String placeId, Pageable pageable) {

		Page<CrewResponse> responses = crewRepository.findAllByPlaceId(placeId, pageable)
			.map(crew -> crewMapper.toCrewResponse(crew, crewMemberRepository.countCrewMemberByCrewId(crew.getId())));

		return new CrewPageResponse(responses);
	}

	@Override
	public CrewResponses getByLocation(SearchCrewRequest distanceRequest) {

		GeometryFactory gf = new GeometryFactory();

		Point location = gf.createPoint(new Coordinate(distanceRequest.longitude(), distanceRequest.latitude()));

		List<CrewResponse> responses = crewRepository.findAllByLocation(location, distanceRequest.distance())
			.stream()
			.map(crew -> crewMapper.toCrewResponse(crew, crewMemberRepository.countCrewMemberByCrewId(crew.getId())))
			.toList();

		return new CrewResponses(responses);
	}

	@Override
	public void updateStatus(UpdateStatusRequest updateStatusRequest) {

		Crew crew = crewRepository.findById(updateStatusRequest.crewId())
			.orElseThrow(() -> new CrewNotFoundException(updateStatusRequest.crewId()));

		crew.changeStatus(updateStatusRequest.status());

	}

}
