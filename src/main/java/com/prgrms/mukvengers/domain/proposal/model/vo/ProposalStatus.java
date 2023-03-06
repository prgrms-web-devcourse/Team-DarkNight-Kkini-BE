package com.prgrms.mukvengers.domain.proposal.model.vo;

import java.util.Arrays;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.prgrms.mukvengers.domain.crew.model.vo.CrewStatus;

public enum ProposalStatus {
	WAITING("대기중"),
	APPROVE("승인"),
	REFUSE("거절");

	private final String status;

	ProposalStatus(String status) {
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
