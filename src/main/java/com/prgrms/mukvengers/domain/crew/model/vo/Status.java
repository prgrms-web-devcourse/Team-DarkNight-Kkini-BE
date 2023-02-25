package com.prgrms.mukvengers.domain.crew.model.vo;

import java.util.Arrays;

public enum Status {
	RECRUITING("모집중");

	private final String status;

	Status(String status) {
		this.status = status;
	}

	public String getStatus() {
		return status;
	}

	public static Status getStatus(String status) {
		return Arrays.stream(Status.values())
			.filter(a -> a.status.equals(status))
			.findAny()
			.orElseThrow();
	}
}
