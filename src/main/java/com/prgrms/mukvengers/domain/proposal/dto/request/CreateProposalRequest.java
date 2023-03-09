package com.prgrms.mukvengers.domain.proposal.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public record CreateProposalRequest(@NotNull Long leaderId,
									@NotBlank String content) {
}
