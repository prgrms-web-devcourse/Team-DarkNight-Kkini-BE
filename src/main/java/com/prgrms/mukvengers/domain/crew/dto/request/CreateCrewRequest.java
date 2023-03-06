package com.prgrms.mukvengers.domain.crew.dto.request;

import java.time.LocalDateTime;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public record CreateCrewRequest(
	@NotBlank String placeId,
	@NotBlank String name,
	@NotBlank String longitude,
	@NotBlank String latitude,
	@NotNull LocalDateTime promiseTime,
	@Min(value = 2) @Max(value = 8) Integer capacity,
	@NotBlank String content,
	@NotBlank String category
) {
}
