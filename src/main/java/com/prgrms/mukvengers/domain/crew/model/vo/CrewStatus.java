package com.prgrms.mukvengers.domain.crew.model.vo;

import java.util.Arrays;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum CrewStatus {

	RECRUITING("모집 중"),
	CLOSE("모집 종료"),
	FINISH("식사 완료");

	private final String status;

	CrewStatus(String status) {
		this.status = status;
	}

	@JsonCreator
	public static CrewStatus of(String statusName) {
		return Arrays.stream(CrewStatus.values())
			.filter(status -> Objects.equals(status.getStatus(), statusName))
			.findFirst()
			.orElseThrow();
	}

	@JsonValue
	public String getStatus() {
		return status;
	}
}
