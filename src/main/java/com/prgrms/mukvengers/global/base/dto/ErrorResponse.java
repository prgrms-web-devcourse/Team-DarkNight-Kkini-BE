package com.prgrms.mukvengers.global.base.dto;

import com.prgrms.mukvengers.global.base.exception.ErrorCode;

public record ErrorResponse(
	String code,
	String message
) {

	public static ErrorResponse of(ErrorCode code) {
		return new ErrorResponse(code.getCode(), code.getMessage());
	}

}
