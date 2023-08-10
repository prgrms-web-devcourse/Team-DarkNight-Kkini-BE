package com.prgrms.mukvengers.domain.crewmember.mapper;

import com.prgrms.mukvengers.domain.crew.model.Crew;
import com.prgrms.mukvengers.domain.crewmember.dto.response.CrewMemberResponse;
import com.prgrms.mukvengers.domain.crewmember.dto.response.MyCrewMemberResponse;
import com.prgrms.mukvengers.domain.crewmember.model.CrewMember;
import com.prgrms.mukvengers.domain.crewmember.model.vo.CrewMemberRole;
import com.prgrms.mukvengers.domain.user.model.User;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-08-10T11:38:02+0900",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.8 (Amazon.com Inc.)"
)
@Component
public class CrewMemberMapperImpl implements CrewMemberMapper {

    @Override
    public CrewMember toCrewMember(Crew crew, Long userId, CrewMemberRole crewMemberRole) {
        if ( crew == null && userId == null && crewMemberRole == null ) {
            return null;
        }

        CrewMember.CrewMemberBuilder crewMember = CrewMember.builder();

        crewMember.crew( crew );
        crewMember.userId( userId );
        crewMember.crewMemberRole( crewMemberRole );

        return crewMember.build();
    }

    @Override
    public CrewMemberResponse toCrewMemberResponse(User user, CrewMemberRole crewMemberRole, Long crewMemberId) {
        if ( user == null && crewMemberRole == null && crewMemberId == null ) {
            return null;
        }

        Long userId = null;
        String nickname = null;
        String profileImgUrl = null;
        if ( user != null ) {
            userId = user.getId();
            nickname = user.getNickname();
            profileImgUrl = user.getProfileImgUrl();
        }
        CrewMemberRole crewMemberRole1 = null;
        crewMemberRole1 = crewMemberRole;
        Long id = null;
        id = crewMemberId;

        CrewMemberResponse crewMemberResponse = new CrewMemberResponse( id, userId, nickname, profileImgUrl, crewMemberRole1 );

        return crewMemberResponse;
    }

    @Override
    public MyCrewMemberResponse toMyCrewMemberResponse(String profileImgUrl, Long crewMemberId, String nickname) {
        if ( profileImgUrl == null && crewMemberId == null && nickname == null ) {
            return null;
        }

        String profileImgUrl1 = null;
        profileImgUrl1 = profileImgUrl;
        Long id = null;
        id = crewMemberId;
        String nickname1 = null;
        nickname1 = nickname;

        MyCrewMemberResponse myCrewMemberResponse = new MyCrewMemberResponse( id, profileImgUrl1, nickname1 );

        return myCrewMemberResponse;
    }
}
