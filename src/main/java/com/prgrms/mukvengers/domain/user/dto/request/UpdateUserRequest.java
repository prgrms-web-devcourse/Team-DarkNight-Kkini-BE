package com.prgrms.mukvengers.domain.user.dto.request;

import javax.validation.constraints.NotBlank;

public record UpdateUserRequest(@NotBlank String nickName,
                                @NotBlank String profileImgUrl,
                                @NotBlank String introduction
) {
}