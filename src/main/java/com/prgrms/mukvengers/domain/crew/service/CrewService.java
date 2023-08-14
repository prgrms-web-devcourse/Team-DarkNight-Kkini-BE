package com.prgrms.mukvengers.domain.crew.service;

import com.prgrms.mukvengers.domain.crew.dto.request.CreateCrewRequest;
import com.prgrms.mukvengers.domain.crew.dto.request.SearchCrewRequest;
import com.prgrms.mukvengers.domain.crew.dto.response.*;
import com.prgrms.mukvengers.domain.crew.exception.CrewNotFoundException;
import com.prgrms.mukvengers.domain.crew.mapper.CrewMapper;
import com.prgrms.mukvengers.domain.crew.model.Crew;
import com.prgrms.mukvengers.domain.crew.model.vo.CrewStatus;
import com.prgrms.mukvengers.domain.crew.repository.CrewRepository;
import com.prgrms.mukvengers.domain.crew.strategy.CrewStatusUpdater;
import com.prgrms.mukvengers.domain.crewmember.dto.response.CrewMemberResponse;
import com.prgrms.mukvengers.domain.crewmember.dto.response.MyCrewMemberResponse;
import com.prgrms.mukvengers.domain.crewmember.exception.MemberNotFoundException;
import com.prgrms.mukvengers.domain.crewmember.mapper.CrewMemberMapper;
import com.prgrms.mukvengers.domain.crewmember.model.CrewMember;
import com.prgrms.mukvengers.domain.crewmember.model.vo.CrewMemberRole;
import com.prgrms.mukvengers.domain.crewmember.repository.CrewMemberRepository;
import com.prgrms.mukvengers.domain.proposal.model.vo.ProposalStatus;
import com.prgrms.mukvengers.domain.proposal.repository.ProposalRepository;
import com.prgrms.mukvengers.domain.store.dto.response.StoreResponse;
import com.prgrms.mukvengers.domain.store.exception.StoreNotFoundException;
import com.prgrms.mukvengers.domain.store.mapper.StoreMapper;
import com.prgrms.mukvengers.domain.store.model.Store;
import com.prgrms.mukvengers.domain.store.repository.StoreRepository;
import com.prgrms.mukvengers.domain.user.exception.UserNotFoundException;
import com.prgrms.mukvengers.domain.user.model.User;
import com.prgrms.mukvengers.domain.user.repository.UserRepository;
import com.prgrms.mukvengers.global.utils.GeometryUtils;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.prgrms.mukvengers.domain.proposal.model.vo.ProposalStatus.NOT_APPLIED;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CrewService {

    private final CrewRepository crewRepository;
    private final UserRepository userRepository;
    private final StoreRepository storeRepository;
    private final CrewMemberRepository crewMemberRepository;
    private final ProposalRepository proposalRepository;
    private final CrewMapper crewMapper;
    private final StoreMapper storeMapper;
    private final CrewMemberMapper crewMemberMapper;
    private final CrewStatusUpdater crewStatusUpdater;
    private final GeometryFactory gf = GeometryUtils.getInstance();

    @Transactional
    public Long create(CreateCrewRequest createCrewRequest, Long userId, Long storeId) {

        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new StoreNotFoundException(storeId));

        Crew crew = crewMapper.toCrew(createCrewRequest, store);

        CrewMember crewMember = crewMemberMapper.toCrewMember(crew, userId, CrewMemberRole.LEADER);
        crew.addCrewMember(crewMember);

        crewRepository.save(crew);

        return crew.getId();
    }

    public CrewResponses<MyCrewResponse> getByUserId(Long userId) {
        List<MyCrewResponse> responses = crewMemberRepository.findAllByUserIdAndNotBlockedOrderByPromiseTime(userId)
                .stream()
                .map(crew -> crewMapper.toMyCrewResponse(
                        crew,
                        crew.getCrewMembers().size(),
                        getProfileImgUrl(crew)
                )).toList();
        return new CrewResponses<>(responses);

    }

    private List<MyCrewMemberResponse> getProfileImgUrl(Crew crew) {
        return crew.getCrewMembers()
                .stream()
                .map(crewMember -> {
                    User user = userRepository.findById(crewMember.getUserId())
                            .orElseThrow(() -> new UserNotFoundException(crewMember.getUserId()));
                    return crewMemberMapper.toMyCrewMemberResponse(
                            user.getProfileImgUrl(), crewMember.getId(), user.getNickname()
                    );

                }).toList();
    }

    public CrewDetailResponse getById(Long userId, Long crewId) {
        Crew crew = crewRepository.findById(crewId)
                .orElseThrow(() -> new CrewNotFoundException(crewId));

        int currentMember = crew.getCrewMembers().size();

        List<CrewMemberResponse> members = crew.getCrewMembers()
                .stream()
                .map(crewMember -> crewMemberMapper.toCrewMemberResponse(
                        userRepository.findById(crewMember.getUserId())
                                .orElseThrow(() -> new UserNotFoundException(crewMember.getUserId())),
                        crewMember.getCrewMemberRole(),
                        crewMember.getId()))
                .toList();

        return crewMapper.toCrewDetailResponse(
                crew,
                currentMember,
                members,
                storeMapper.toStoreResponse(crew.getStore()),
                proposalRepository.findByUserIdAndCrewId(userId, crewId).orElseGet(() -> NOT_APPLIED));
    }

    public CrewPageResponse getByPlaceId(Long userId, String placeId, Pageable pageable) {
        Page<Crew> crews = crewRepository.findAllByPlaceId(placeId, pageable);
        List<CrewDetailResponse> responses = crews.stream()
                .map(crew -> createCrewDetailResponse(userId, crew))
                .collect(Collectors.toList());

        return new CrewPageResponse(new PageImpl<>(responses, pageable, crews.getTotalElements()));
    }

    private CrewDetailResponse createCrewDetailResponse(Long userId, Crew crew) {
        Long crewId = crew.getId();
        Integer crewMemberCount = crewMemberRepository.countCrewMemberByCrewId(crewId);
        List<CrewMemberResponse> crewMembers = getCrewMembers(crewId);
        StoreResponse storeResponse = storeMapper.toStoreResponse(crew.getStore());
        ProposalStatus proposalstatus = proposalRepository.findByUserIdAndCrewId(userId, crewId)
                .orElse(NOT_APPLIED);
        return crewMapper.toCrewDetailResponse(crew, crewMemberCount, crewMembers, storeResponse, proposalstatus);
    }

    private List<CrewMemberResponse> getCrewMembers(Long crewId) {
        return crewMemberRepository.findAllByCrewId(crewId).stream()
                .map(this::createCrewMemberResponse)
                .toList();
    }

    private CrewMemberResponse createCrewMemberResponse(CrewMember crewMember) {
        Long userId = crewMember.getUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        return crewMemberMapper.toCrewMemberResponse(user, crewMember.getCrewMemberRole(), crewMember.getId());
    }

    public CrewLocationResponses getByLocation(SearchCrewRequest distanceRequest) {

        Point location = gf.createPoint(new Coordinate(distanceRequest.longitude(), distanceRequest.latitude()));

        List<CrewLocationResponse> responses = crewRepository.findAllByLocation(location, distanceRequest.distance())
                .stream()
                .map(crew -> crewMapper.toCrewLocationResponse(crew.getLocation(), crew.getStore().getId(),
                        crew.getStore().getPlaceName()))
                .collect(Collectors.toList());

        return new CrewLocationResponses(responses);
    }

    public CrewLocationResponses getByLocationWithIndex(SearchCrewRequest distanceRequest) {

        Coordinate coordinate = new Coordinate(distanceRequest.longitude(), distanceRequest.latitude());

        Point location = gf.createPoint(coordinate);
        Double radius = GeometryUtils.calculateApproximateRadius(distanceRequest.distance());

        List<CrewLocationResponse> responses = crewRepository.findAllByLocation(location, radius)
                .stream()
                .map(crew -> crewMapper.toCrewLocationResponse(crew.getLocation(), crew.getStore().getId(),
                        crew.getStore().getPlaceName()))
                .toList();

        return new CrewLocationResponses(responses);
    }

    @Transactional
    public CrewStatusResponse updateStatus(Long crewId, Long userId, CrewStatus crewStatus) {

        Crew crew = crewRepository.findById(crewId)
                .orElseThrow(() -> new CrewNotFoundException(crewId));

        CrewMember crewMember = crewMemberRepository.findCrewMemberByCrewIdAndUserId(crewId, userId)
                .orElseThrow(() -> new MemberNotFoundException(userId));

        crewMember.isNotLeader();

        crewStatusUpdater.updateStatus(crew, crewMember, crewStatus);

        return new CrewStatusResponse(crew.getStatus());
    }

}
