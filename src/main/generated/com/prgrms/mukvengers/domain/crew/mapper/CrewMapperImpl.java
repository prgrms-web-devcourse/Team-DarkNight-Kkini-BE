package com.prgrms.mukvengers.domain.crew.mapper;

import com.prgrms.mukvengers.domain.crew.dto.request.CreateCrewRequest;
import com.prgrms.mukvengers.domain.crew.dto.response.CrewDetailResponse;
import com.prgrms.mukvengers.domain.crew.dto.response.CrewLocationResponse;
import com.prgrms.mukvengers.domain.crew.dto.response.CrewResponse;
import com.prgrms.mukvengers.domain.crew.dto.response.MyCrewResponse;
import com.prgrms.mukvengers.domain.crew.model.Crew;
import com.prgrms.mukvengers.domain.crew.model.vo.CrewStatus;
import com.prgrms.mukvengers.domain.crewmember.dto.response.CrewMemberResponse;
import com.prgrms.mukvengers.domain.crewmember.dto.response.MyCrewMemberResponse;
import com.prgrms.mukvengers.domain.proposal.model.vo.ProposalStatus;
import com.prgrms.mukvengers.domain.store.dto.response.StoreResponse;
import com.prgrms.mukvengers.domain.store.model.Store;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-08-10T11:38:02+0900",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.8 (Amazon.com Inc.)"
)
@Component
public class CrewMapperImpl implements CrewMapper {

    @Override
    public Crew toCrew(CreateCrewRequest createCrewRequest, Store store) {
        if ( createCrewRequest == null && store == null ) {
            return null;
        }

        Crew.CrewBuilder crew = Crew.builder();

        if ( createCrewRequest != null ) {
            crew.category( createCrewRequest.category() );
            crew.promiseTime( createCrewRequest.promiseTime() );
            crew.name( createCrewRequest.name() );
            crew.capacity( createCrewRequest.capacity() );
            crew.content( createCrewRequest.content() );
        }
        if ( store != null ) {
            crew.store( store );
            crew.location( store.getLocation() );
        }

        return crew.build();
    }

    @Override
    public CrewResponse toCrewResponse(Crew crew, Integer currentMember) {
        if ( crew == null && currentMember == null ) {
            return null;
        }

        LocalDateTime promiseTime = null;
        Long id = null;
        String name = null;
        Integer capacity = null;
        CrewStatus status = null;
        String content = null;
        String category = null;
        if ( crew != null ) {
            promiseTime = crew.getPromiseTime();
            id = crew.getId();
            name = crew.getName();
            capacity = crew.getCapacity();
            status = crew.getStatus();
            content = crew.getContent();
            category = crew.getCategory();
        }
        Integer currentMember1 = null;
        currentMember1 = currentMember;

        CrewResponse crewResponse = new CrewResponse( id, name, currentMember1, capacity, status, content, category, promiseTime );

        return crewResponse;
    }

    @Override
    public CrewDetailResponse toCrewDetailResponse(Crew crew, Integer currentMember, List<CrewMemberResponse> members, StoreResponse storeResponse, ProposalStatus proposalStatus) {
        if ( crew == null && currentMember == null && members == null && storeResponse == null && proposalStatus == null ) {
            return null;
        }

        LocalDateTime promiseTime = null;
        Long id = null;
        CrewStatus crewStatus = null;
        String name = null;
        Integer capacity = null;
        String content = null;
        String category = null;
        if ( crew != null ) {
            promiseTime = crew.getPromiseTime();
            id = crew.getId();
            crewStatus = crew.getStatus();
            name = crew.getName();
            capacity = crew.getCapacity();
            content = crew.getContent();
            category = crew.getCategory();
        }
        Integer currentMember1 = null;
        currentMember1 = currentMember;
        List<CrewMemberResponse> members1 = null;
        List<CrewMemberResponse> list = members;
        if ( list != null ) {
            members1 = new ArrayList<CrewMemberResponse>( list );
        }
        StoreResponse response = null;
        response = storeResponse;
        ProposalStatus proposalStatus1 = null;
        proposalStatus1 = proposalStatus;

        CrewDetailResponse crewDetailResponse = new CrewDetailResponse( id, response, name, currentMember1, capacity, crewStatus, content, category, promiseTime, members1, proposalStatus1 );

        return crewDetailResponse;
    }

    @Override
    public MyCrewResponse toMyCrewResponse(Crew crew, Integer currentMember, List<MyCrewMemberResponse> members) {
        if ( crew == null && currentMember == null && members == null ) {
            return null;
        }

        LocalDateTime promiseTime = null;
        String name = null;
        String placeName = null;
        CrewStatus crewStatus = null;
        Long id = null;
        Integer capacity = null;
        String content = null;
        String category = null;
        if ( crew != null ) {
            promiseTime = crew.getPromiseTime();
            name = crew.getName();
            placeName = crewStorePlaceName( crew );
            crewStatus = crew.getStatus();
            id = crew.getId();
            capacity = crew.getCapacity();
            content = crew.getContent();
            category = crew.getCategory();
        }
        Integer currentMember1 = null;
        currentMember1 = currentMember;
        List<MyCrewMemberResponse> members1 = null;
        List<MyCrewMemberResponse> list = members;
        if ( list != null ) {
            members1 = new ArrayList<MyCrewMemberResponse>( list );
        }

        MyCrewResponse myCrewResponse = new MyCrewResponse( id, placeName, name, currentMember1, capacity, crewStatus, content, category, promiseTime, members1 );

        return myCrewResponse;
    }

    @Override
    public CrewLocationResponse toCrewLocationResponse(Point location, Long storeId, String placeName) {
        if ( location == null && storeId == null && placeName == null ) {
            return null;
        }

        Double latitude = null;
        Double longitude = null;
        if ( location != null ) {
            latitude = mapLatitude( location );
            longitude = mapLongitude( location );
        }
        Long storeId1 = null;
        storeId1 = storeId;
        String placeName1 = null;
        placeName1 = placeName;

        CrewLocationResponse crewLocationResponse = new CrewLocationResponse( longitude, latitude, storeId1, placeName1 );

        return crewLocationResponse;
    }

    private String crewStorePlaceName(Crew crew) {
        if ( crew == null ) {
            return null;
        }
        Store store = crew.getStore();
        if ( store == null ) {
            return null;
        }
        String placeName = store.getPlaceName();
        if ( placeName == null ) {
            return null;
        }
        return placeName;
    }
}
