package com.prgrms.mukvengers.domain.notification.model.vo;

public enum MessageKey {
	PROPOSAL_ARRIVED("proposal.arrived"),
	PROPOSAL_ACCEPTED("proposal.accepted"),
	PROPOSAL_REJECTED("proposal.rejected");

	private final String key;

	MessageKey(String key) {
		this.key = key;
	}

	public String getKey() {
		return key;
	}
}
