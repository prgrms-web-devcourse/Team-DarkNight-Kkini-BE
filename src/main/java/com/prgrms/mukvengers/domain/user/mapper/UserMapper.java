package com.prgrms.mukvengers.domain.user.mapper;

import static org.mapstruct.ReportingPolicy.*;

import org.mapstruct.Mapper;

import com.prgrms.mukvengers.domain.user.dto.response.UserProfileResponse;
import com.prgrms.mukvengers.domain.user.model.User;

@Mapper(componentModel = "spring", unmappedSourcePolicy = IGNORE)
public interface UserMapper {

	UserProfileResponse toSingleUserResponse(User user);

}
