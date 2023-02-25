package com.prgrms.mukvengers.domain.crew.dto.request;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public record CreateCrewRequest(
	@NotNull String mapStoreId,
	@NotBlank String name,
	@NotBlank String latitude,
	@NotBlank String longitude,
	@Min(value = 2) @Max(value = 8) Integer capacity,
	@NotBlank String status,
	@NotBlank String content,
	@NotBlank String category
) {
}
