package com.prgrms.mukvengers.domain.store.dto.request;

import javax.validation.constraints.NotBlank;

public record CreateStoreRequest(@NotBlank String longitude,
                                 @NotBlank String latitude,
                                 @NotBlank String mapStoreId
) {
}
