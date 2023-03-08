package com.prgrms.mukvengers.global.common.dto;

import com.prgrms.mukvengers.global.exception.ErrorCode;

public record ErrorResponse(
	String code,
	String message
) {

	public static ErrorResponse of(ErrorCode code) {
		return new ErrorResponse(code.getCode(), code.getMessage());
	}

}
