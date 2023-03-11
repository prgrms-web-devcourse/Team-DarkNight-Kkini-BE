package com.prgrms.mukvengers.domain.crew.service;

import static com.prgrms.mukvengers.domain.proposal.model.vo.ProposalStatus.*;

import java.util.List;
import java.util.stream.Collectors;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prgrms.mukvengers.domain.crew.dto.event.CreateCrewEvent;
import com.prgrms.mukvengers.domain.crew.dto.request.CreateCrewRequest;
import com.prgrms.mukvengers.domain.crew.dto.request.SearchCrewRequest;
import com.prgrms.mukvengers.domain.crew.dto.response.CrewDetailResponse;
import com.prgrms.mukvengers.domain.crew.dto.response.CrewLocationResponse;
import com.prgrms.mukvengers.domain.crew.dto.response.CrewLocationResponses;
import com.prgrms.mukvengers.domain.crew.dto.response.CrewPageResponse;
import com.prgrms.mukvengers.domain.crew.dto.response.CrewResponses;
import com.prgrms.mukvengers.domain.crew.dto.response.CrewStatusResponse;
import com.prgrms.mukvengers.domain.crew.exception.CrewNotFoundException;
import com.prgrms.mukvengers.domain.crew.exception.CrewStatusException;
import com.prgrms.mukvengers.domain.crew.mapper.CrewMapper;
import com.prgrms.mukvengers.domain.crew.model.Crew;
import com.prgrms.mukvengers.domain.crew.model.vo.CrewStatus;
import com.prgrms.mukvengers.domain.crew.repository.CrewRepository;
import com.prgrms.mukvengers.domain.crewmember.dto.response.CrewMemberResponse;
import com.prgrms.mukvengers.domain.crewmember.exception.MemberNotFoundException;
import com.prgrms.mukvengers.domain.crewmember.exception.NotLeaderException;
import com.prgrms.mukvengers.domain.crewmember.mapper.CrewMemberMapper;
import com.prgrms.mukvengers.domain.crewmember.model.CrewMember;
import com.prgrms.mukvengers.domain.crewmember.model.vo.CrewMemberRole;
import com.prgrms.mukvengers.domain.crewmember.repository.CrewMemberRepository;
import com.prgrms.mukvengers.domain.proposal.repository.ProposalRepository;
import com.prgrms.mukvengers.domain.store.mapper.StoreMapper;
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
	private final CrewMemberRepository crewMemberRepository;
	private final ProposalRepository proposalRepository;
	private final CrewMapper crewMapper;
	private final StoreMapper storeMapper;
	private final CrewMemberMapper crewMemberMapper;
	private final ApplicationEventPublisher publisher;

	@Override
	@Transactional
	public IdResponse create(CreateCrewRequest createCrewRequest, Long userId) {

		userRepository.findById(userId)
			.orElseThrow(() -> new UserNotFoundException(userId));

		Store store = storeRepository.findByPlaceId(createCrewRequest.createStoreRequest().placeId())
			.orElseGet(() -> storeRepository.save(storeMapper.toStore(createCrewRequest.createStoreRequest())));

		Crew crew = crewMapper.toCrew(createCrewRequest, store);

		crewRepository.save(crew);

		publisher.publishEvent(new CreateCrewEvent(userId, crew.getId()));

		return new IdResponse(crew.getId());
	}

	@Override
	public CrewResponses getByUserId(Long userId) {

		List<CrewDetailResponse> responses = crewMemberRepository.findAllByUserIdOrderByStatus(userId)
			.stream()
			.map(crew -> crewMapper.toCrewDetailResponse(
				crew,
				crewMemberRepository.countCrewMemberByCrewId(crew.getId()), crewMemberRepository.findAllByCrewId(
						crew.getId())
					.stream()
					.map(CrewMember -> crewMemberMapper.toCrewMemberResponse(
						userRepository.findById(CrewMember.getUserId())
							.orElseThrow(() -> new UserNotFoundException(CrewMember.getUserId())),
						CrewMember.getCrewMemberRole()))
					.toList(), storeMapper.toStoreResponse(crew.getStore()),
				proposalRepository.findByUserIdAndCrewId(userId, crew.getId()).orElseGet(() -> NOT_APPLIED)))
			.toList();

		return new CrewResponses(responses);
	}

	@Override
	public CrewDetailResponse getById(Long userId, Long crewId) {

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

		return crewMapper.toCrewDetailResponse(crew, currentMember, members,
			storeMapper.toStoreResponse(crew.getStore()),
			proposalRepository.findByUserIdAndCrewId(userId, crewId).orElseGet(() -> NOT_APPLIED));
	}

	@Override
	public CrewPageResponse getByPlaceId(Long userId, String placeId, Pageable pageable) {

		Page<CrewDetailResponse> responses = crewRepository.findAllByPlaceId(placeId, pageable)
			.map(crew -> crewMapper.toCrewDetailResponse(crew,
				crewMemberRepository.countCrewMemberByCrewId(crew.getId())
				, crewMemberRepository.findAllByCrewId(crew.getId())
					.stream()
					.map(CrewMember -> crewMemberMapper.toCrewMemberResponse(
						userRepository.findById(CrewMember.getUserId())
							.orElseThrow(() -> new UserNotFoundException(CrewMember.getUserId())),
						CrewMember.getCrewMemberRole()))
					.toList(), storeMapper.toStoreResponse(crew.getStore()),
				proposalRepository.findByUserIdAndCrewId(userId,
					crew.getId()).orElseGet(() -> NOT_APPLIED)));

		return new CrewPageResponse(responses);
	}

	@Override
	public CrewLocationResponses getByLocation(SearchCrewRequest distanceRequest) {

		GeometryFactory gf = new GeometryFactory();

		Point location = gf.createPoint(new Coordinate(distanceRequest.longitude(), distanceRequest.latitude()));

		List<CrewLocationResponse> responses = crewRepository.findAllByLocation(location, distanceRequest.distance())
			.stream()
			.map(crew -> crewMapper.toCrewLocationResponse(crew.getLocation(), crew.getStore().getId(),
				crew.getStore().getPlaceName()))
			.collect(Collectors.toList());

		return new CrewLocationResponses(responses);
	}

	@Override
	@Transactional
	public CrewStatusResponse updateStatus(Long crewId, Long userId, CrewStatus crewStatus) {

		Crew crew = crewRepository.findById(crewId)
			.orElseThrow(() -> new CrewNotFoundException(crewId));

		CrewMember crewMember = crewMemberRepository.findCrewMemberByCrewIdAndUserId(crewId, userId)
			.orElseThrow(() -> new MemberNotFoundException(userId));

		if (!crewMember.getCrewMemberRole().equals(CrewMemberRole.LEADER)) {
			throw new NotLeaderException(CrewMemberRole.LEADER);
		}

		switch (crewStatus) {
			case CLOSE -> {
				if (!crew.getStatus().equals(CrewStatus.RECRUITING)) {
					throw new CrewStatusException(crew.getStatus());
				}
			}

			case FINISH -> {
				if (!crew.getStatus().equals(CrewStatus.CLOSE)) {
					throw new CrewStatusException(crew.getStatus());
				}
			}

			default -> {
				throw new CrewStatusException(crewStatus);
			}
		}

		crew.changeStatus(crewStatus);

		return new CrewStatusResponse(crew.getStatus());
	}
}
