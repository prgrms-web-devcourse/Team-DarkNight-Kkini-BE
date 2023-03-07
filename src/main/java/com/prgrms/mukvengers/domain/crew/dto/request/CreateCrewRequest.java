package com.prgrms.mukvengers.domain.crew.dto.request;

import java.time.LocalDateTime;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.prgrms.mukvengers.domain.store.dto.request.CreateStoreRequest;

public record CreateCrewRequest(

	@NotNull CreateStoreRequest createStoreRequest,
	@NotBlank String name,
	@NotNull LocalDateTime promiseTime,
	@Min(value = 2) @Max(value = 8) Integer capacity,
	@NotBlank String content,
	@NotBlank String category
) {
}
