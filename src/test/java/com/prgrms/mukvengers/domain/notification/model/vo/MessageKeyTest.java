package com.prgrms.mukvengers.domain.notification.model.vo;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MessageKeyTest {

	@Test
	@DisplayName("[성공] 메시지 프로퍼티에 저장된 키 값을 성공적으로 가져온다.")
	void getKey() {
		// Given
		MessageKey arrived = MessageKey.PROPOSAL_ARRIVED;
		MessageKey accepted = MessageKey.PROPOSAL_ACCEPTED;
		MessageKey rejected = MessageKey.PROPOSAL_REJECTED;

		// When
		String arrivedKey = arrived.getKey();
		String acceptedKey = accepted.getKey();
		String rejectedKey = rejected.getKey();

		// Then
		assertEquals("proposal.arrived", arrivedKey);
		assertEquals("proposal.accepted", acceptedKey);
		assertEquals("proposal.rejected", rejectedKey);
	}
}