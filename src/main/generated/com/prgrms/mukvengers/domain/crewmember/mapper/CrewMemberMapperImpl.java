package com.prgrms.mukvengers.domain.crewmember.mapper;

import com.prgrms.mukvengers.domain.crew.model.Crew;
import com.prgrms.mukvengers.domain.crewmember.dto.response.CrewMemberResponse;
import com.prgrms.mukvengers.domain.crewmember.model.CrewMember;
import com.prgrms.mukvengers.domain.crewmember.model.vo.CrewMemberRole;
import com.prgrms.mukvengers.domain.user.model.User;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-04-02T18:01:10+0900",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.5 (Oracle Corporation)"
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
}
