package com.prgrms.mukvengers.domain.proposal.model.vo;

import java.util.Arrays;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonValue;
import com.prgrms.mukvengers.domain.proposal.exception.InvalidProposalStatusException;

public enum ProposalStatus {
	WAITING("대기 중"),
	APPROVE("승인"),
	REFUSE("거절"),
	NOT_APPLIED("미신청");

	private final String status;

	ProposalStatus(String status) {
		this.status = status;
	}

	public static ProposalStatus of(String statusName) {
		return Arrays.stream(ProposalStatus.values())
			.filter(status -> Objects.equals(status.getStatus(), statusName))
			.findFirst()
			.orElseThrow(() -> new InvalidProposalStatusException(statusName));
	}

	@JsonValue
	public String getStatus() {
		return status;
	}
}
