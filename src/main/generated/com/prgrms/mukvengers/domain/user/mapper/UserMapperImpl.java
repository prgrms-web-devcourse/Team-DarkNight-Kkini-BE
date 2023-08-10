package com.prgrms.mukvengers.domain.user.mapper;

import com.prgrms.mukvengers.domain.user.dto.response.UserProfileResponse;
import com.prgrms.mukvengers.domain.user.model.User;
import com.prgrms.mukvengers.global.security.oauth.dto.OAuthUserInfo;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-08-10T11:38:02+0900",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.8 (Amazon.com Inc.)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public UserProfileResponse toSingleUserResponse(User user) {
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

    @Override
    public User toUser(OAuthUserInfo oauthUserInfo) {
        if ( oauthUserInfo == null ) {
            return null;
        }

        User.UserBuilder user = User.builder();

        user.nickname( oauthUserInfo.nickname() );
        user.profileImgUrl( oauthUserInfo.profileImgUrl() );
        user.provider( oauthUserInfo.provider() );
        user.oauthId( oauthUserInfo.oauthId() );

        return user.build();
    }
}
