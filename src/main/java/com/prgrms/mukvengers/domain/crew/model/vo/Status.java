package com.prgrms.mukvengers.domain.crew.model.vo;

import java.util.Arrays;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Status {
	RECRUITING("모집중"),
	CLOSE("모집종료");

	private final String status;

	Status(String status) {
		this.status = status;
	}

	@JsonCreator
	public static Status of(String statusName) {
		return Arrays.stream(Status.values())
			.filter(status -> Objects.equals(status.getStatus(), statusName))
			.findFirst()
			.orElseThrow();
	}

	@JsonValue
	public String getStatus() {
		return status;
	}
}
