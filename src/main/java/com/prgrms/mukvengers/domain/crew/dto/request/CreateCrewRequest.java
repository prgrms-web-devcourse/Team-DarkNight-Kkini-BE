package com.prgrms.mukvengers.domain.crew.dto.request;

import java.time.LocalDateTime;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.prgrms.mukvengers.domain.store.dto.request.CreateStoreRequest;

public record CreateCrewRequest(

	@NotNull CreateStoreRequest createStoreRequest,
	@NotBlank(message = "이름을 입력해 주세요") String name,
	@NotNull(message = "약속 시간을 입력해 주세요") LocalDateTime promiseTime,
	@Min(value = 2, message = "정원은 {value}명 이상이어야 합니다.") @Max(value = 8, message = "정원은 {value}명 이하여야 합니다. ") Integer capacity,
	@NotBlank(message = "내용을 입력해 주세요") String content,
	@NotBlank(message = "카테고리를 입력해 주세요") String category
) {
}
