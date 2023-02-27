package com.prgrms.mukvengers.global.security.oauth.mapper;

import org.mapstruct.Mapper;

import com.prgrms.mukvengers.domain.user.model.User;
import com.prgrms.mukvengers.global.security.oauth.dto.OAuthUserInfo;

@Mapper(componentModel = "spring")
public interface OAuthMapper { // TODO: UserMapper로 옮겨야 하나?

	User toUser(OAuthUserInfo oauthUserInfo);

}