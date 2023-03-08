package com.prgrms.mukvengers.domain.review.dto.response;

import java.time.LocalDateTime;

import com.prgrms.mukvengers.domain.crew.dto.response.CrewResponse;
import com.prgrms.mukvengers.domain.user.dto.response.UserProfileResponse;

public record ReviewResponse(UserProfileResponse reviewer,
							 UserProfileResponse reviewee,
							 CrewResponse crew,
							 LocalDateTime promiseTime,
							 String content,
							 Integer mannerScore,
							 Integer tasteScore
							 ) {
}
