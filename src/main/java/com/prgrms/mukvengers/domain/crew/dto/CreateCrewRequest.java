package com.prgrms.mukvengers.domain.crew.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public record CreateCrewRequest(
	@NotNull String mapStoreId,
	@NotBlank String name,
	@NotBlank String latitude,
	@NotBlank String longitude,
	@Size(min = 2, max = 8) Integer capacity,
	@NotBlank String status,
	@NotBlank String content,
	@NotBlank String category
) {
}
