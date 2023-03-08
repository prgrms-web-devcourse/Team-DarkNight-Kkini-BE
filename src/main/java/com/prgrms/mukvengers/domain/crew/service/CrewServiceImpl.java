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
import com.prgrms.mukvengers.domain.crew.dto.response.CrewDetailResponse;
import com.prgrms.mukvengers.domain.crew.dto.response.CrewPageResponse;
import com.prgrms.mukvengers.domain.crew.dto.response.CrewResponses;
import com.prgrms.mukvengers.domain.crew.exception.CrewNotFoundException;
import com.prgrms.mukvengers.domain.crew.mapper.CrewMapper;
import com.prgrms.mukvengers.domain.crew.model.Crew;
import com.prgrms.mukvengers.domain.crew.repository.CrewRepository;
import com.prgrms.mukvengers.domain.crewmember.dto.response.CrewMemberResponse;
import com.prgrms.mukvengers.domain.crewmember.mapper.CrewMemberMapper;
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

	private final CrewMemberMapper crewMemberMapper;

	@Override
	@Transactional
	public IdResponse create(CreateCrewRequest createCrewRequest, Long userId) {

		userRepository.findById(userId)
			.orElseThrow(() -> new UserNotFoundException(userId));

		Store store = storeRepository.findByPlaceId(createCrewRequest.createStoreRequest().placeId())
			.orElseGet(() -> storeRepository.save(storeMapper.toStore(createCrewRequest.createStoreRequest())));

		Crew crew = crewMapper.toCrew(createCrewRequest, store);

		crewRepository.save(crew);

		return new IdResponse(crew.getId());
	}

	@Override
	public CrewResponses getByUserId(Long userId) {

		List<CrewDetailResponse> responses = crewMemberRepository.findAllByUserIdOrderByStatus(userId)
			.stream()
			.map(crew -> crewMapper.toCrewAndCrewMemberResponse(
				crew,
				crewMemberRepository.countCrewMemberByCrewId(crew.getId()), crewMemberRepository.findAllByCrewId(
						crew.getId())
					.stream()
					.map(CrewMember -> crewMemberMapper.toCrewMemberResponse(
						userRepository.findById(CrewMember.getUserId())
							.orElseThrow(() -> new UserNotFoundException(CrewMember.getUserId())),
						CrewMember.getCrewMemberRole()))
					.toList()))
			.toList();

		User user = userRepository.findById(userId)
			.orElseThrow(() -> new UserNotFoundException(userId));

		return new CrewResponses(responses);
	}

	@Override
	public CrewDetailResponse getById(Long crewId) {

		Crew crew = crewRepository.findById(crewId)
			.orElseThrow(() -> new CrewNotFoundException(crewId));

		Integer currentMember = crewMemberRepository.countCrewMemberByCrewId(crewId);

		List<CrewMemberResponse> members = crewMemberRepository.findAllByCrewId(crewId)
			.stream()
			.map(CrewMember -> crewMemberMapper.toCrewMemberResponse(
				userRepository.findById(CrewMember.getUserId())
					.orElseThrow(() -> new UserNotFoundException(CrewMember.getUserId())),
				CrewMember.getCrewMemberRole()))
			.toList();

		return crewMapper.toCrewAndCrewMemberResponse(crew, currentMember, members);
	}

	@Override
	public CrewPageResponse getByPlaceId(String placeId, Pageable pageable) {

		Page<CrewDetailResponse> responses = crewRepository.findAllByPlaceId(placeId, pageable)
			.map(crew -> crewMapper.toCrewAndCrewMemberResponse(crew,
				crewMemberRepository.countCrewMemberByCrewId(crew.getId())
				, crewMemberRepository.findAllByCrewId(crew.getId())
					.stream()
					.map(CrewMember -> crewMemberMapper.toCrewMemberResponse(
						userRepository.findById(CrewMember.getUserId())
							.orElseThrow(() -> new UserNotFoundException(CrewMember.getUserId())),
						CrewMember.getCrewMemberRole()))
					.toList()));

		return new CrewPageResponse(responses);
	}

	@Override
	public CrewResponses getByLocation(SearchCrewRequest distanceRequest) {

		GeometryFactory gf = new GeometryFactory();

		Point location = gf.createPoint(new Coordinate(distanceRequest.longitude(), distanceRequest.latitude()));

		List<CrewDetailResponse> responses = crewRepository.findAllByLocation(location,
				distanceRequest.distance())
			.stream()
			.map(crew -> crewMapper.toCrewAndCrewMemberResponse(crew,
				crewMemberRepository.countCrewMemberByCrewId(crew.getId()),
				crewMemberRepository.findAllByCrewId(
						crew.getId())
					.stream()
					.map(CrewMember -> crewMemberMapper.toCrewMemberResponse(
						userRepository.findById(CrewMember.getUserId())
							.orElseThrow(() -> new UserNotFoundException(CrewMember.getUserId())),
						CrewMember.getCrewMemberRole()))
					.toList()))
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
