package com.prgrms.mukvengers.domain.proposal.mapper;

import com.prgrms.mukvengers.domain.proposal.dto.request.CreateProposalRequest;
import com.prgrms.mukvengers.domain.proposal.dto.response.ProposalResponse;
import com.prgrms.mukvengers.domain.proposal.model.Proposal;
import com.prgrms.mukvengers.domain.proposal.model.vo.ProposalStatus;
import com.prgrms.mukvengers.domain.user.dto.response.UserProfileResponse;
import com.prgrms.mukvengers.domain.user.model.User;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-08-10T11:38:02+0900",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.8 (Amazon.com Inc.)"
)
@Component
public class ProposalMapperImpl implements ProposalMapper {

    @Override
    public Proposal toProposal(CreateProposalRequest proposalRequest, User user, Long crewId) {
        if ( proposalRequest == null && user == null && crewId == null ) {
            return null;
        }

        Proposal.ProposalBuilder proposal = Proposal.builder();

        if ( proposalRequest != null ) {
            proposal.leaderId( proposalRequest.leaderId() );
            proposal.content( proposalRequest.content() );
        }
        proposal.user( user );
        proposal.crewId( crewId );

        return proposal.build();
    }

    @Override
    public ProposalResponse toProposalResponse(Proposal proposal, String storeName, String crewName) {
        if ( proposal == null && storeName == null && crewName == null ) {
            return null;
        }

        Long id = null;
        UserProfileResponse user = null;
        Long leaderId = null;
        Long crewId = null;
        String content = null;
        ProposalStatus status = null;
        if ( proposal != null ) {
            id = proposal.getId();
            user = userToUserProfileResponse( proposal.getUser() );
            leaderId = proposal.getLeaderId();
            crewId = proposal.getCrewId();
            content = proposal.getContent();
            status = proposal.getStatus();
        }
        String storeName1 = null;
        storeName1 = storeName;
        String crewName1 = null;
        crewName1 = crewName;

        ProposalResponse proposalResponse = new ProposalResponse( id, user, leaderId, crewId, content, status, storeName1, crewName1 );

        return proposalResponse;
    }

    protected UserProfileResponse userToUserProfileResponse(User user) {
        if ( user == null ) {
            return null;
        }

        Long id = null;
        String nickname = null;
        String profileImgUrl = null;
        String introduction = null;
        Integer leaderCount = null;
        Integer crewCount = null;
        Integer tasteScore = null;
        String mannerScore = null;

        id = user.getId();
        nickname = user.getNickname();
        profileImgUrl = user.getProfileImgUrl();
        introduction = user.getIntroduction();
        leaderCount = user.getLeaderCount();
        crewCount = user.getCrewCount();
        tasteScore = user.getTasteScore();
        if ( user.getMannerScore() != null ) {
            mannerScore = user.getMannerScore().toString();
        }

        UserProfileResponse userProfileResponse = new UserProfileResponse( id, nickname, profileImgUrl, introduction, leaderCount, crewCount, tasteScore, mannerScore );

        return userProfileResponse;
    }
}
