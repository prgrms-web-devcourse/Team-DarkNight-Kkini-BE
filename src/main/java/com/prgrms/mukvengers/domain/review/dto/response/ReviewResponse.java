package com.prgrms.mukvengers.domain.review.dto.response;

import java.time.LocalDateTime;

import com.prgrms.mukvengers.domain.store.dto.response.StoreResponse;
import com.prgrms.mukvengers.domain.user.dto.response.UserProfileResponse;

public record ReviewResponse(UserProfileResponse reviewer,
							 UserProfileResponse reviewee,
							 StoreResponse store,
							 String crewName,
							 LocalDateTime promiseTime,
							 String content,
							 Integer mannerPoint,
							 Integer tastePoint
							 ) {
}
